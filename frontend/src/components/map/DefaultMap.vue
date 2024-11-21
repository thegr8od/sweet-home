<template>
  <div class="map-container">
    <div id="map" ref="mapContainer"></div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, onBeforeUnmount } from 'vue'
import { useHouseStore } from '@/stores/houseStore'
import { storeToRefs } from 'pinia'

// Store 사용
const houseStore = useHouseStore()
const { selectedPosition, markerPositions, houses } = storeToRefs(houseStore)

// 지도 관련 변수
const mapContainer = ref(null)
let map = null
let markers = []
let infowindow = null

// 외부에서 호출할 수 있는 메서드 정의
const setCenter = (position) => {
  if (map && position) {
    const moveLatLng = new window.kakao.maps.LatLng(position.lat, position.lng)
    map.panTo(moveLatLng)
  }
}

// 메서드를 외부로 노출
defineExpose({ setCenter })

// 맵 리사이즈 이벤트 핸들러
const handleMapResize = () => {
  if (map && selectedPosition.value) {
    const moveLatLng = new window.kakao.maps.LatLng(
      selectedPosition.value.lat,
      selectedPosition.value.lng,
    )
    map.panTo(moveLatLng)
  }
}

// selectedPosition 변경 감지
watch(
  selectedPosition,
  (newPosition) => {
    console.log('Watch - selectedPosition changed:', newPosition)
    if (newPosition && map) {
      // 지도 레벨을 조정하고 위치로 이동
      map.setLevel(3) // 원하는 레벨로 설정
      moveToPosition(newPosition)
    }
  },
  { deep: true },
)

// markerPositions 변경 감지 추가
watch(
  markerPositions,
  (newPositions) => {
    console.log('markerPositions이 변경되었습니다:', newPositions)
    if (newPositions && map) {
      updateMarkers(newPositions)
    }
  },
  { deep: true },
)

// 지도 초기화
onMounted(() => {
  loadKakaoMap()
  // 윈도우 리사이즈 이벤트 리스너 추가
  window.addEventListener('resize', handleMapResize)
})

// 언마운트 시 정리
onBeforeUnmount(() => {
  markers.forEach((marker) => marker.setMap(null))
  if (infowindow) infowindow.close()
  map = null
  // 윈도우 리사이즈 이벤트 리스너 제거
  window.removeEventListener('resize', handleMapResize)
})

// Kakao Map 로드
function loadKakaoMap() {
  const script = document.createElement('script')
  script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${import.meta.env.VITE_KAKAO_MAP_API_KEY}&libraries=services,clusterer`
  script.onload = () => {
    window.kakao.maps.load(() => {
      initMap()
    })
  }
  document.head.appendChild(script)
}

// 지도 초기화 메서드
function initMap() {
  if (!mapContainer.value) {
    console.error('Map container not found')
    return
  }

  const options = {
    center: new window.kakao.maps.LatLng(35.8751717287353, 128.570018325571),
    level: 5,
  }

  map = new window.kakao.maps.Map(mapContainer.value, options)

  if (markerPositions.value.length > 0) {
    updateMarkers(markerPositions.value)
  }
}

// 위치로 이동하는 메서드
function moveToPosition(position) {
  if (!map || !position) return

  const moveLatLng = new window.kakao.maps.LatLng(position.lat, position.lng)
  map.panTo(moveLatLng)

  // 이동이 완료된 후 한 번만 인포윈도우 표시
  setTimeout(() => {
    showInfoWindow(position)
  }, 300)
}

// 인포윈도우 표시 메서드
function showInfoWindow(position) {
  if (!map || !position) return

  if (infowindow) {
    infowindow.close()
  }

  const markerIndex = markerPositions.value.findIndex(
    (pos) =>
      Math.abs(pos[0] - position.lat) < 0.0000001 && Math.abs(pos[1] - position.lng) < 0.0000001,
  )

  if (markerIndex !== -1) {
    const house = houses.value[markerIndex]
    const marker = markers[markerIndex]
    const recentDeal = houseStore.aptDeals?.[0] || {}

    if (house && marker) {
      infowindow = new window.kakao.maps.InfoWindow({
        content: `
          <div style="padding: 12px; width: 250px; font-family: 'Spoqa Han Sans Neo', sans-serif;">
            <div style="
              border-bottom: 2px solid #93c5fd;
              margin-bottom: 10px;
              padding-bottom: 6px;
            ">
              <h4 style="
                margin: 0 0 3px 0;
                font-size: 15px;
                font-weight: 600;
                color: #3b82f6;
              ">${house.aptName}</h4>
              <p style="
                margin: 0;
                font-size: 12px;
                color: #94a3b8;
              ">${house.legalDong}</p>
            </div>

            <div style="
              display: grid;
              grid-template-columns: repeat(2, 1fr);
              gap: 6px;
              font-size: 12px;
            ">
              <div style="color: #64748b;">
                <span style="color: #94a3b8;">건축년도</span><br>
                <strong>${houseStore.aptInfo?.buildYear || '정보없음'}</strong>
              </div>
              <div style="color: #64748b;">
                <span style="color: #94a3b8;">전용면적</span><br>
                <strong>${recentDeal.excluUseAr ? recentDeal.excluUseAr + '㎡' : '정보없음'}</strong>
              </div>
              <div style="
                grid-column: span 2;
                margin-top: 4px;
                padding-top: 6px;
                border-top: 1px solid #e2e8f0;
                text-align: right;
              ">
                <span style="
                  font-size: 14px;
                  font-weight: 600;
                  color: #3b82f6;
                ">${recentDeal.tradeAmount ? formatPrice(recentDeal.tradeAmount) : '정보없음'}</span>
              </div>
            </div>
          </div>
        `,
      })
      infowindow.open(map, marker)
    }
  }
}

// 가격 포맷팅 함수 추가
function formatPrice(price) {
  if (!price) return '가격정보 없음'
  const amount = price.toString().replace(/,/g, '')
  if (parseInt(amount) >= 10000) {
    const uk = Math.floor(parseInt(amount) / 10000)
    const rest = parseInt(amount) % 10000
    return rest > 0 ? `${uk}억 ${rest}만원` : `${uk}억원`
  }
  return price + '만원'
}

// 마커 업데이트 메서드
function updateMarkers(positions) {
  // 기존 마커 제거
  markers.forEach((marker) => marker.setMap(null))
  markers = []

  // 새로운 마커 생성
  positions.forEach((position, index) => {
    const markerPosition = new window.kakao.maps.LatLng(position[0], position[1])
    const marker = new window.kakao.maps.Marker({
      position: markerPosition,
      map: map,
    })

    // 마커 클릭 이벤트
    window.kakao.maps.event.addListener(marker, 'click', () => {
      const house = houses.value[index]
      if (house) {
        // 클릭한 마커의 위치 정보로 selectedPosition 업데이트
        houseStore.setSelectedPosition({
          lat: position[0],
          lng: position[1],
        })

        // 선택된 집 정보 업데이트 및 상세 패널 표시
        houseStore.setSelectedHouse(house)
        houseStore.showDetail = true

        if (house.aptSeq) {
          console.log('맵: house.aptSeq:', house.aptSeq)
          houseStore.getDetail({ aptSeq: house.aptSeq })
        }
      }
    })

    markers.push(marker)
  })

  // 모든 마커가 보이도록 지도 범위 재설정
  if (positions.length > 0) {
    const bounds = new window.kakao.maps.LatLngBounds()
    positions.forEach((position) => {
      bounds.extend(new window.kakao.maps.LatLng(position[0], position[1]))
    })
    map.setBounds(bounds)
  }
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
