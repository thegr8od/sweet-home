<template>
  <div class="map-container">
    <div id="map"></div>
  </div>
</template>

<script>
export default {
  name: 'DefaultMap',
  data() {
    return {
      infowindow: null,
      position: {
        lat: 33.450701, // 초기 위치 (위도, 경도)
        lng: 126.570667,
      },
      itemMarker: null,
      map: null,
      center: null,
      apiKey: import.meta.env.VITE_KAKAO_MAP_API_KEY,
    }
  },
  mounted() {
    const script = document.createElement('script')
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${this.apiKey}&libraries=services`

    // 스크립트 로드가 완료된 후 초기화 함수 실행
    script.onload = () => {
      // kakao 객체가 로드되었는지 확인 후 초기화
      if (window.kakao) {
        kakao.maps.load(this.initMap)
      } else {
        console.error('Kakao Maps API 로드 실패')
      }
    }

    document.head.appendChild(script)
  },
  methods: {
    initMap() {
      const container = document.getElementById('map')
      const options = {
        center: new kakao.maps.LatLng(this.position.lat, this.position.lng),
        level: 5, // 지도 확대 수준
      }

      this.map = new kakao.maps.Map(container, options)

      const markerPosition = new kakao.maps.LatLng(this.position.lat, this.position.lng)
      this.itemMarker = new kakao.maps.Marker({
        position: markerPosition,
      })

      this.itemMarker.setMap(this.map)
    },
    changeSize(size) {
      const container = document.getElementById('map')
      container.style.width = `${size}px`
      container.style.height = `${size}px`
      this.map.relayout() // 지도 크기 변경 후 재배치
    },
    displayInfoWindow() {
      if (this.infowindow && this.infowindow.getMap()) {
        this.map.setCenter(this.infowindow.getPosition())
        return
      }

      const iwContent = '<div style="padding:5px;">Hello World!</div>'
      const iwPosition = new kakao.maps.LatLng(33.450701, 126.570667)
      const iwRemoveable = true

      this.infowindow = new kakao.maps.InfoWindow({
        map: this.map,
        position: iwPosition,
        content: iwContent,
        removable: iwRemoveable,
      })

      this.map.setCenter(iwPosition)
    },
  },
}
</script>

<style scoped>
.map-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

#map {
  width: 100%;
  height: 100vh;
}
</style>
