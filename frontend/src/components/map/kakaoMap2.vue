<template>
  <div>
    <div id="map"></div>
    <div class="category-group">
      <button
        v-for="category in categories"
        :key="category.code"
        @click="searchCategory(category.code)"
        :class="{ active: selectedCategory === category.code }"
      >
        {{ category.name }}
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'KakaoMap2',
  data() {
    return {
      infowindow: null,
      position: {
        lat: 33.450701,
        lng: 126.570667,
      },
      itemMarker: null,
      map: null,
      ps: null,
      markers: [],
      selectedCategory: null,
      categories: [
        { name: '지하철역', code: 'SW8' },
        { name: '버스정류장', code: 'SW9' },
      ],
      currentCategory: null,
    }
  },
  mounted() {
    const script = document.createElement('script')
    /* global kakao */
    script.onload = () => kakao.maps.load(this.initMap)
    script.src =
      '//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=ff39ee8d511b533648d3baac6dd7efff&libraries=services'
    document.head.appendChild(script)
  },
  methods: {
    initMap() {
      const container = document.getElementById('map')
      const options = {
        center: new kakao.maps.LatLng(this.position.lat, this.position.lng),
        level: 5,
      }

      this.map = new kakao.maps.Map(container, options)
      this.ps = new kakao.maps.services.Places(this.map)

      kakao.maps.event.addListener(this.map, 'zoom_changed', () => {
        if (this.currentCategory) {
          this.searchCategory(this.currentCategory)
        }
      })

      kakao.maps.event.addListener(this.map, 'dragend', () => {
        if (this.currentCategory) {
          this.searchCategory(this.currentCategory)
        }
      })

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
      this.map.relayout()
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
    searchCategory(categoryCode) {
      this.markers.forEach((marker) => marker.setMap(null))
      this.markers = []

      if (this.selectedCategory === categoryCode) {
        this.selectedCategory = null
        this.currentCategory = null
        return
      }

      this.selectedCategory = categoryCode
      this.currentCategory = categoryCode

      const center = this.map.getCenter()
      const bounds = this.map.getBounds()

      this.ps.categorySearch(
        categoryCode,
        (data, status) => {
          if (status === kakao.maps.services.Status.OK) {
            data.forEach((place) => {
              const marker = new kakao.maps.Marker({
                position: new kakao.maps.LatLng(place.y, place.x),
                map: this.map,
              })

              kakao.maps.event.addListener(marker, 'click', () => {
                if (this.infowindow) this.infowindow.close()

                this.infowindow = new kakao.maps.InfoWindow({
                  content: `
                    <div style="padding:5px;font-size:12px;">
                      <strong>${place.place_name}</strong><br>
                      ${place.category_name}<br>
                      <span style="color:#888;">${place.distance}m</span>
                    </div>
                  `,
                  removable: true,
                })
                this.infowindow.open(this.map, marker)
              })

              this.markers.push(marker)
            })
          }
        },
        {
          location: center,
          bounds: bounds,
          radius: 2000,
        },
      )
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
  height: calc(100vh - 60px);
}

.button-group {
  margin: 10px 0px;
}

button {
  margin: 0 3px;
}

.category-group {
  margin: 10px 0px;
}

.category-group button {
  margin: 0 3px;
}

.category-group button.active {
  background-color: #007bff;
  color: #fff;
}
</style>
