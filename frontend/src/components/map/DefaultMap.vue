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
const { selectedPosition, markerPositions, interestMarkerPositions, houses } = storeToRefs(houseStore)


// 지도 관련 변수
const mapContainer = ref(null)
let map = null
let markers = []
let interestMarkers = []
let infowindow = null
let clusterer = null

// let bounds = {
//   ha: 0,
//   oa: 0,
//   pa: 0,
//   qa: 0
// }

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

// markerPositions watch 수정
watch(
  markerPositions,
  (newPositions) => {
    console.log('일반 마커 위치가 변경되었습니다:', newPositions)
    if (map) {
      updateAllMarkers(newPositions)
    }
  },
  { deep: true }
)

// interestMarkerPositions watch 추가
watch(
  interestMarkerPositions,
  (newInterestPositions) => {
    console.log('관심 마커 위치가 변경되었습니다:', newInterestPositions)
    if (map) {
      // 기존 관심 마커 제거
      interestMarkers.forEach((marker) => marker.setMap(null))
      interestMarkers = []
      
      // 새로운 관심 마커 생성
      newInterestPositions.forEach((position, index) => {
        createMarker(position, index, true)
      })
    }
  },
  { deep: true }
)

// 지도 초기화
onMounted(() => {
  loadKakaoMap()
  // 윈도우 리사이즈 이벤트 리스너 추가
  window.addEventListener('resize', handleMapResize)
})

// 언마운트 시 정리 수정
onBeforeUnmount(() => {
  markers.forEach((marker) => marker.setMap(null))
  interestMarkers.forEach((marker) => marker.setMap(null))
  if (infowindow) infowindow.close()
  map = null
  window.removeEventListener('resize', handleMapResize)
})

// Kakao Map 로드
function loadKakaoMap() {
  const script = document.createElement('script')
  script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${import.meta.env.VITE_KAKAO_MAP_API_KEY}&libraries=clusterer`
  script.onload = () => {
    window.kakao.maps.load(() => {
      initMap()
      updateArea()
    })
  }
  document.head.appendChild(script)
}

// 지도 초기화 메서드 수정
function initMap() {
  if (!mapContainer.value) {
    console.error('Map container not found')
    return
  }

  const options = {
    center: new window.kakao.maps.LatLng(35.8751717287353, 128.570018325571),
    level: 3,
  }

  map = new window.kakao.maps.Map(mapContainer.value, options)

  // 마커 클러스터러를 생성합니다 
  clusterer = new window.kakao.maps.MarkerClusterer({
    map: map, // 마커들을 클러스터로 관리하고 표시할 지도 객체 
    averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정 
    minLevel: 5 // 클러스터 할 최소 지도 레벨 
  });

  // 먼저 일반 마커 생성
  if (markerPositions.value.length > 0) {
    markerPositions.value.forEach((position, index) => {
      createMarker(position, index, false)
    })
  }

  // 그 다음 관심 마커 생성 (위에 표시되도록)
  if (interestMarkerPositions.value.length > 0) {
    interestMarkerPositions.value.forEach((position, index) => {
      createMarker(position, index, true)
    })
  }


  window.kakao.maps.event.addListener(map, 'zoom_changed', function() {
    console.log('zoom_changed')
    // 인포윈도우가 열려있다면 닫기
    if (infowindow) {
      infowindow.close()
    }
    updateArea()
  })

  window.kakao.maps.event.addListener(map, 'dragend', function() {
    console.log('dragend')
    // 인포윈도우가 열려있다면 닫기
    if (infowindow) {
      infowindow.close()
    }
    updateArea()
  })
}

function updateArea() {
  console.log('checkArea')
  houseStore.clearMarkerPositions()

  var level = map.getLevel();
  console.log('level:', level);

  var limit = 100

  if (level <= 3){
    limit = 5000
  }else if (level <= 4){
    limit = 1000
  }else if (level <= 5){
    limit = 300
  }else if (level <= 6){
    limit = 200
  }else if (level <= 7){
    limit = 100
  }else if (level == 8){
    limit = 30
  }else if (level == 9){
    limit = 40
  }else if (level == 10){
    limit = 50
  }
  console.log('limit:', limit)

  if (map) {
    const bounds = map.getBounds()
    const params = {
      minLat: bounds.getSouthWest().getLat(),
      maxLat: bounds.getNorthEast().getLat(),
      minLng: bounds.getSouthWest().getLng(),
      maxLng: bounds.getNorthEast().getLng(),
      limit: limit
    }
    console.log('지도 영역:', params)
    houseStore.onBoundsChanged(params)
    updateAllMarkers(markerPositions.value)
    clusterer.addMarkers(markers)

  }

}



// 마커 생성 함수 수정
function createMarker(position, index, isInterest) {
  const markerPosition = new window.kakao.maps.LatLng(position[0], position[1])
  
  let marker
  if (isInterest) {
    // 관심 마커 이미지 설정 - 크기를 일반 마커와 비슷하게 조정
    const imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png'
    const imageSize = new window.kakao.maps.Size(27.42, 40)  // 크기를 24x35로 변경
    const markerImage = new window.kakao.maps.MarkerImage(imageSrc, imageSize)
    
    marker = new window.kakao.maps.Marker({
      position: markerPosition,
      image: markerImage,
      zIndex: 2
    })
  } else {
    // 일반 마커는 기본 마커 사용
    marker = new window.kakao.maps.Marker({
      position: markerPosition,
      zIndex: 1
    })
  }

  // 마커를 지도에 표시
  marker.setMap(map)

  // 마커 클릭 이벤트
  window.kakao.maps.event.addListener(marker, 'click', () => {
    // 관심 마커인 경우와 일반 마커인 경우를 구분
    updateArea()
    let targetHouse
    if (isInterest) {
      const interestStore = useInterestStore()
      targetHouse = interestStore.interests.find(interest => 
        parseFloat(interest.latitude) === position[0] && 
        parseFloat(interest.longitude) === position[1]
      )
      // 관심 마커 클릭 시 해당 위치로 이동하고 줌 레벨 조정
      map.setLevel(3)
      map.panTo(markerPosition)
    } else {
      targetHouse = houses.value[index]
    }

    if (targetHouse) {
      houseStore.setSelectedPosition({
        lat: position[0],
        lng: position[1],
      })

      houseStore.setSelectedHouse(targetHouse)

      if (targetHouse.aptSeq) {
        console.log('맵: aptSeq:', targetHouse.aptSeq)
        houseStore.getDetail({ aptSeq: targetHouse.aptSeq })
      }
    }
  })

  if (isInterest) {
    interestMarkers.push(marker)
  } else {
    markers.push(marker)
  }

}

// 일반 마커만 업데이트하는 함수 수정
function updateAllMarkers(positions) {
  // 기존 일반 마커만 제거
  markers.forEach((marker) => marker.setMap(null))
  markers = []

  // 일반 마커 생성 (zIndex를 1로 설정)
  positions.forEach((position, index) => {
    createMarker(position, index, false)
  })

  // 관심 마커 재생성 (zIndex를 2로 설정하여 항상 위에 표시)
  if (interestMarkerPositions.value.length > 0) {
    interestMarkerPositions.value.forEach((position, index) => {
      createMarker(position, index, true)
    })
  }

  // // 일반 마커가 있을 경우에만 bounds 설정
  // if (positions.length > 0) {
  //   const bounds = new window.kakao.maps.LatLngBounds()
  //   positions.forEach((position) => {
  //     bounds.extend(new window.kakao.maps.LatLng(position[0], position[1]))
  //   })
  //   map.setBounds(bounds)
  // }
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

// 인포윈도우 표시 메서드 수정
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
        zIndex: 3
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
