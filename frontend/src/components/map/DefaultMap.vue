<template>
  <div class="map-container">
    <div id="map" ref="mapContainer"></div>
  </div>
</template>

<script>
import { useHouseStore } from '@/stores/houseStore'
import { storeToRefs } from 'pinia'

export default {
  name: 'DefaultMap',
  data() {
    return {
      infowindow: null,
      position: {
        lat: 35.8751717287353,
        lng: 128.570018325571,
      },
      markers: [],
      map: null,
      center: null,
      apiKey: import.meta.env.VITE_KAKAO_MAP_API_KEY,
    }
  },
  setup() {
    const houseStore = useHouseStore()
    const { markerPositions, houses, selectedPosition } = storeToRefs(houseStore)
    return { markerPositions, houses, selectedPosition }
  },
  watch: {
    markerPositions: {
      handler(newPositions) {
        this.updateMarkers(newPositions)
      },
      deep: true,
    },
    selectedPosition: {
      handler(newPosition) {
        if (newPosition && this.map) {
          this.moveToPosition(newPosition)
        }
      },
      deep: true,
    },
  },
  mounted() {
    this.$nextTick(() => {
      if (window.kakao && window.kakao.maps) {
        this.initMap()
      } else {
        this.loadKakaoMap()
      }
    })
  },
  methods: {
    loadKakaoMap() {
      const script = document.createElement('script')
      script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${this.apiKey}&libraries=services,clusterer`
      script.onload = () => {
        window.kakao.maps.load(() => {
          this.$nextTick(() => {
            this.initMap()
          })
        })
      }
      document.head.appendChild(script)
    },
    initMap() {
      if (!this.$refs.mapContainer) {
        console.error('Map container not found')
        return
      }

      const options = {
        center: new window.kakao.maps.LatLng(this.position.lat, this.position.lng),
        level: 5,
      }

      this.map = new window.kakao.maps.Map(this.$refs.mapContainer, options)

      if (this.markerPositions.length > 0) {
        this.updateMarkers(this.markerPositions)
      }
    },
    updateMarkers(positions) {
      this.markers.forEach((marker) => marker.setMap(null))
      this.markers = []

      positions.forEach((position, index) => {
        const markerPosition = new window.kakao.maps.LatLng(position[0], position[1])
        const marker = new window.kakao.maps.Marker({
          position: markerPosition,
          map: this.map,
        })

        window.kakao.maps.event.addListener(marker, 'click', () => {
          const house = this.houses[index]
          if (house) {
            if (this.infowindow) this.infowindow.close()

            this.infowindow = new window.kakao.maps.InfoWindow({
              content: `
                <div style="padding:10px;width:200px;">
                  <h4 style="margin:0 0 5px;font-size:14px;">${house.aptName}</h4>
                  <p style="margin:0;font-size:12px;">${house.legalDong}</p>
                </div>
              `,
            })
            this.infowindow.open(this.map, marker)
          }
        })

        this.markers.push(marker)
      })

      if (positions.length > 0) {
        const bounds = new window.kakao.maps.LatLngBounds()
        positions.forEach((position) => {
          bounds.extend(new window.kakao.maps.LatLng(position[0], position[1]))
        })
        this.map.setBounds(bounds)
      }
    },
    moveToPosition(position) {
      if (!this.map || !position) return

      console.log('Moving to position:', position)

      const moveLatLng = new window.kakao.maps.LatLng(position.lat, position.lng)

      // 왼쪽 패널의 너비를 고려하여 오프셋 계산
      const leftPanelWidth = document.querySelector('.left-panel')?.offsetWidth || 0
      const mapWidth = this.map.getContainer().offsetWidth
      const offset = leftPanelWidth / 2

      // 지도 중심점을 약간 오른쪽으로 이동
      const adjustedLatLng = new window.kakao.maps.LatLng(
        position.lat,
        position.lng + (offset / mapWidth) * 0.1, // 경도 값 조정
      )

      // 현재 지도 레벨을 가져옵니다
      const currentLevel = this.map.getLevel()
      const targetLevel = 3

      // 조정된 위치로 부드럽게 이동
      this.map.panTo(adjustedLatLng)

      // 줌 레벨 조정
      if (currentLevel > targetLevel) {
        const smoothZoom = () => {
          const level = this.map.getLevel()
          if (level > targetLevel) {
            this.map.setLevel(level - 1)
            setTimeout(smoothZoom, 100)
          }
        }
        setTimeout(smoothZoom, 300)
      }

      // 인포윈도우 표시
      const markerIndex = this.markerPositions.findIndex((pos) => {
        return (
          pos[0].toFixed(7) === position.lat.toFixed(7) &&
          pos[1].toFixed(7) === position.lng.toFixed(7)
        )
      })

      console.log('Marker index:', markerIndex)

      if (markerIndex !== -1) {
        const house = this.houses[markerIndex]
        const marker = this.markers[markerIndex]

        if (this.infowindow) this.infowindow.close()

        this.infowindow = new window.kakao.maps.InfoWindow({
          content: `
            <div style="padding:10px;width:200px;">
              <h4 style="margin:0 0 5px;font-size:14px;">${house.aptName}</h4>
              <p style="margin:0;font-size:12px;">${house.legalDong}</p>
            </div>
          `,
        })

        setTimeout(() => {
          this.infowindow.open(this.map, marker)
        }, 500)
      }
    },
  },
  beforeUnmount() {
    this.markers.forEach((marker) => marker.setMap(null))
    if (this.infowindow) this.infowindow.close()
    this.map = null
  },
}
</script>

<style scoped>
.map-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  height: 100%;
}

#map {
  width: 100%;
  height: 100vh;
  position: relative;
}
</style>
