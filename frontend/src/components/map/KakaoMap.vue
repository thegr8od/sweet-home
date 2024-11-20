<!-- <template>
  <div>
    <div id="map" ref="map"></div>
    <div class="map_wrap" style="z-index: 3">
      <ul id="category">
        <li id="BK9" data-order="0">은행</li>
        <li id="MT1" data-order="1">마트</li>
        <li id="PM9" data-order="2">약국</li>
        <li id="OL7" data-order="3">주유소</li>
        <li id="CE7" data-order="4">카페</li>
        <li id="CS2" data-order="5">편의점</li>
      </ul>
    </div>
  </div>
</template>

<script>
import { useHouseStore } from '@/stores/houseStore'
import { useAddressStore } from '@/stores/addressStore'
import { BUS } from '@/store/modules/EventBus'

export default {
  name: 'KakaoMap',
  data() {
    return {
      bounds: {
        qa: null,
        pa: null,
        ha: null,
        oa: null,
      },
      markers: [],
      infowindow: null,
      place: null,
      currCategory: '', // 현재 선택된 카테고리
      placeOverlay: null,
      contentNode: null,
      center: '',
      marker: null,
      position: {
        lat: '',
        lng: '',
      },
      itemMarker: '',
    }
  },
  computed: {
    houseStore: useHouseStore(),
    addressStore: useAddressStore(),
    houses() {
      return this.houseStore.houses
    },
    markerPositions() {
      return this.houseStore.markerPositions
    },
  },
  created() {
    BUS.$on('change-hposition', (position) => {
      this.position = position
      this.changePosition(position)
    })
  },
  mounted() {
    this.initMap()
    this.getBounds()

    BUS.$on('moveDong', (dong) => {
      this.place.keywordSearch(dong, this.dongSearchCB)
    })
  },
  methods: {
    changePosition(position) {
      const adjLng = parseFloat(position.lng) - 0.005 // 센터 경도 조정
      this.center = new kakao.maps.LatLng(position.lat, adjLng)
      this.marker = new kakao.maps.LatLng(position.lat, position.lng)
      this.map.panTo(this.center)

      const imageSrc = 'https://ifh.cc/g/rxQT1B.png'
      const imageSize = new kakao.maps.Size(64, 69)
      const imageOption = { offset: new kakao.maps.Point(27, 69) }

      const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption)

      this.itemMarker = new kakao.maps.Marker({
        map: this.map,
        position: this.marker,
        image: markerImage,
      })
      this.itemMarker.setMap(this.map)
    },

    getBounds() {
      kakao.maps.load(() => {
        const bounds = this.map.getBounds()
        this.bounds.qa = bounds.qa
        this.bounds.pa = bounds.pa
        this.bounds.ha = bounds.ha
        this.bounds.oa = bounds.oa
        this.houseStore.addressHouse(this.bounds) // Pinia 액션 호출
        this.displayMarker(this.markerPositions)
      })
    },

    initMap() {
      kakao.maps.load(() => {
        const container = document.getElementById('map')
        const options = {
          center: new kakao.maps.LatLng(37.5283169, 126.9294254),
          level: 3,
        }

        this.map = new kakao.maps.Map(container, options)

        const marker = new kakao.maps.LatLng(this.position.lat, this.position.lng)
        this.itemMarker = new kakao.maps.Marker({
          position: marker,
        })
        this.itemMarker.setMap(this.map)

        const ps = new kakao.maps.services.Places()
        this.place = ps

        const placeOverlay = new kakao.maps.CustomOverlay({ zIndex: 1 })
        this.placeOverlay = placeOverlay

        const contentNode = document.createElement('div')
        this.contentNode = contentNode

        this.contentNode.className = 'placeinfo_wrap'

        this.addEventHandle(this.contentNode, 'mousedown', kakao.maps.event.preventMap)
      })
    },
  },
}
</script> -->
