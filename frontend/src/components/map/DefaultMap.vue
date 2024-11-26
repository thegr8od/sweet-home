<template>
  <div class="map-container">
    <div id="map" ref="mapContainer"></div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, onBeforeUnmount } from 'vue'
import { useHouseStore } from '@/stores/houseStore'
import { storeToRefs } from 'pinia'
import { useInterestStore } from '@/stores/interestStore'

// Store 사용
const houseStore = useHouseStore()
const { selectedPosition, markerPositions, interestMarkerPositions, houses } =
  storeToRefs(houseStore)
const interestStore = useInterestStore()

// 지도 관련 변수
const mapContainer = ref(null)
let map = null
let markers = []
let overlay_markers = []
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
defineExpose({
  setCenter,
  updateArea,
})

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

// selectedPosition 변경 감지 수정
watch(
  selectedPosition,
  (newPosition) => {
    console.log('Watch - selectedPosition changed:', newPosition)
    if (newPosition && map) {
      // 지도 레벨을 조정하고 위치로 이동
      map.setLevel(3) // 원하는 레벨로 설정
      moveToPosition(newPosition)
    } else if (!newPosition && infowindow) {
      // 선택이 해제된 경우 인포윈도우 닫기
      infowindow.close()
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
  { deep: true },
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
  { deep: true },
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
    minLevel: 8, // 클러스터 할 최소 지도 레벨
  })

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

  window.kakao.maps.event.addListener(map, 'zoom_changed', function () {
    console.log('zoom_changed')
    // 인포윈도우가 열려있다면 닫기
    if (infowindow) {
      infowindow.close()
    }
    var level = map.getLevel()
    if (level >= 8) {
      // 기존 일반 마커만 제거
      overlay_markers.forEach((marker) => marker.setMap(null))
      overlay_markers = []
    } else {
      markers.forEach((marker) => marker.setMap(null))
      markers = []
    }
    updateArea()
  })

  window.kakao.maps.event.addListener(map, 'dragend', function () {
    console.log('dragend')
    // 인포윈도우가 열려있다면 닫기
    if (infowindow) {
      infowindow.close()
    }
    if (houseStore.updateMapFlag) {
      updateArea()
    } else {
      houseStore.setUpdateMapFlag(true)
    }
  })

  // 지도 이동이 완료되면 인포윈도우 표시
  window.kakao.maps.event.addListener(map, 'idle', function () {
    showInfoWindow(selectedPosition.value)
  })
}

function updateArea() {
  console.log('checkArea')
  houseStore.clearMarkerPositions()

  var level = map.getLevel()
  console.log('level:', level)

  var limit = 500
  if (level <= 3) {
    limit = 5000
  } else if (level <= 4) {
    limit = 1000
  } else if (level <= 5) {
    limit = 300
  } else if (level <= 6) {
    limit = 200
  } else if (level <= 7) {
    limit = 100
  }
  console.log('limit:', limit)

  if (map) {
    const bounds = map.getBounds()
    const params = {
      minLat: bounds.getSouthWest().getLat(),
      maxLat: bounds.getNorthEast().getLat(),
      minLng: bounds.getSouthWest().getLng(),
      maxLng: bounds.getNorthEast().getLng(),
      limit: limit,
    }
    console.log('지도 영역:', params)

    // 레벨에 따라 다른 API 호출
    if (level >= 8) {
      // 레벨이 8 이상일 때는 좌표만 받아오는 API 호출
      houseStore.onBoundsChangedCoordinate(params)
    } else {
      // 레벨이 8 미만일 때는 기존 API 호출
      houseStore.onBoundsChanged(params)
    }

    updateAllMarkers(markerPositions.value)
    console.log('clusterer:', clusterer)
  }
}

// 마커 생성 함수 수정
function createMarker(position, index, isInterest) {
  const markerPosition = new window.kakao.maps.LatLng(position[0], position[1])
  var level = map.getLevel()

  // 현재 선택된 위치인지 확인
  const isSelected =
    selectedPosition.value &&
    selectedPosition.value.lat === position[0] &&
    selectedPosition.value.lng === position[1]

  // 선택된 마커인 경우 인포윈도우 표시
  if (isSelected) {
    showInfoWindow(selectedPosition.value)
  }

  let marker
  if (isInterest) {
    if (level < 8) {
      const house = interestStore.interests.find(
        (interest) =>
          parseFloat(interest.latitude) === position[0] &&
          parseFloat(interest.longitude) === position[1],
      )

      // 실거래 정보 가져오기 - interestStore에서 직접 가져옴
      const dealInfo = house ? interestStore.interestDetails[house.aptSeq] : null
      const houseDetail = dealInfo || houseStore.houseDetails[index]

      // 가격 변환 로직 추가
      const formattedPrice = formatPrice(houseDetail?.maxPrice || houseDetail?.tradeAmount)
      const formattedArea =
        houseDetail?.maxPriceArea || houseDetail?.excluUseAr
          ? Math.round(parseFloat(houseDetail.maxPriceArea || houseDetail.excluUseAr) / 3.3)
          : '?'

      const content = document.createElement('div')
      content.className = `inline-flex flex-col items-center w-auto cursor-pointer ${
        isSelected ? 'transform scale-120' : ''
      }`
      content.style.filter = isSelected ? 'drop-shadow(0 4px 6px rgba(0, 0, 0, 0.2))' : ''

      // Top section
      const topSection = document.createElement('div')
      topSection.className =
        'w-16 bg-red-500 text-white px-2 py-0.5 rounded-t-md text-xs font-medium text-center'
      topSection.innerHTML = `<span>${formattedArea}평</span>`

      // Bottom section
      const bottomSection = document.createElement('div')
      bottomSection.className =
        'w-16 bg-white text-red-500 px-2 py-0.5 rounded-b-md text-xs font-medium text-center border border-red-500 border-t-0'
      bottomSection.innerHTML = `<span>${formattedPrice}</span>`

      // // Triangle pointer
      // const triangle = document.createElement('div')
      // triangle.className =
      //   'w-0 h-0 border-l-[5px] border-l-transparent border-r-[5px] border-r-transparent border-t-[6px] border-t-white'
      // triangle.style.filter = 'drop-shadow(0 1px 0 rgb(239 68 68))'

      // 요소들을 조합
      content.appendChild(topSection)
      content.appendChild(bottomSection)
      // content.appendChild(triangle)

      // 클릭 이벤트 추가
      content.onclick = () => {
        handleMarkerClick(position, index, true)
      }

      const overlay_marker = new window.kakao.maps.CustomOverlay({
        position: markerPosition,
        content: content,
        zIndex: isSelected ? 3 : isInterest ? 2 : 1,
      })

      overlay_marker.setMap(map)
      overlay_markers.push(overlay_marker)
    } else {
      // 일반 마커 사용
      const imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png'
      const imageSize = new window.kakao.maps.Size(
        isSelected ? 30.16 : 27.42, // 선택된 경우 크기 증가
        isSelected ? 44 : 40,
      )
      const markerImage = new window.kakao.maps.MarkerImage(imageSrc, imageSize)

      marker = new window.kakao.maps.Marker({
        position: markerPosition,
        image: markerImage,
        zIndex: 2,
      })

      if (isSelected) {
        // 선택된 마커의 경우 크기 증가
        const markerImage = new window.kakao.maps.MarkerImage(
          marker.getImage().src,
          new window.kakao.maps.Size(35, 45), // 기본 크기보다 약간 크게
          marker.getImage().options,
        )
        marker.setImage(markerImage)
      }

      marker.setMap(map)
      interestMarkers.push(marker)
      addMarkerClickEvent(marker, position, index, true)
    }
  } else {
    // 레벨이 5미만 (오버레이 마커 사용)
    if (level < 8) {
      // const house = houses.value[index]
      const houseDetail = houseStore.houseDetails[index]

      // 가격 변환 로직 추가
      const formattedPrice = formatPrice(houseDetail?.maxPrice)
      const formattedArea = houseDetail?.maxPriceArea
        ? Math.round(parseFloat(houseDetail.maxPriceArea) / 3.3)
        : '?' // 제곱미터를 평으로 변환

      // content div 생성
      const content = document.createElement('div')
      content.className = `inline-flex flex-col items-center w-auto cursor-pointer ${
        isSelected ? 'transform scale-120' : ''
      }`
      content.style.filter = isSelected ? 'drop-shadow(0 4px 6px rgba(0, 0, 0, 0.2))' : ''

      // Top section
      const topSection = document.createElement('div')
      topSection.className =
        'w-16 bg-blue-600 text-white px-2 py-0.5 rounded-t-md text-xs font-medium text-center'
      topSection.innerHTML = `<span>${formattedArea}평</span>`

      // Bottom section
      const bottomSection = document.createElement('div')
      bottomSection.className =
        'w-16 bg-white text-blue-600 px-2 py-0.5 rounded-b-md text-xs font-medium text-center border border-blue-600 border-t-0'
      bottomSection.innerHTML = `<span>${formattedPrice}</span>`

      // Triangle pointer
      const triangle = document.createElement('div')
      triangle.className =
        'w-0 h-0 border-l-[5px] border-l-transparent border-r-[5px] border-r-transparent border-t-[6px] border-t-white'
      triangle.style.filter = 'drop-shadow(0 1px 0 rgb(37 99 235))'

      // 요소들을 조합
      content.appendChild(topSection)
      content.appendChild(bottomSection)
      content.appendChild(triangle)

      // 클릭 이벤트 추가
      content.onclick = () => {
        handleMarkerClick(position, index, false)
      }

      const overlay_marker = new window.kakao.maps.CustomOverlay({
        position: markerPosition,
        content: content,
        zIndex: isSelected ? 3 : 1,
      })

      overlay_markers.push(overlay_marker)
      overlay_marker.setMap(map)
    }
    // 레벨이 5이상 (클러스터 사용)
    else {
      // 일반 마커는 기본 마커 사용
      marker = new window.kakao.maps.Marker({
        position: markerPosition,
        zIndex: 1,
      })
      // marker.setMap(map)
      markers.push(marker) // markers 배열은 클러스터에만 사용됨.
      addMarkerClickEvent(marker, position, index, false)
    }
  }
}

function addMarkerClickEvent(marker, position, index, isInterest) {
  window.kakao.maps.event.addListener(marker, 'click', () => {
    handleMarkerClick(position, index, isInterest)
  })
}

function handleMarkerClick(position, index, isInterest) {
  let targetHouse
  if (isInterest) {
    targetHouse = interestStore.interests.find(
      (interest) =>
        parseFloat(interest.latitude) === position[0] &&
        parseFloat(interest.longitude) === position[1],
    )
  } else {
    targetHouse = houses.value[index]
  }

  if (targetHouse) {
    const newPosition = {
      lat: position[0],
      lng: position[1],
    }
    houseStore.setSelectedPosition(newPosition)
    houseStore.setSelectedHouse(targetHouse)

    if (targetHouse.aptSeq) {
      houseStore.getDetail({ aptSeq: targetHouse.aptSeq })
    }

    // 인포윈도우 표시 추가
    showInfoWindow(newPosition)
  }
  updateArea()
}

// 일반 마커만 업데이트하는 함수 수정
function updateAllMarkers(positions) {
  // 기존 마커들 제거
  markers.forEach((marker) => marker.setMap(null))
  markers = []
  overlay_markers.forEach((marker) => marker.setMap(null))
  overlay_markers = []
  interestMarkers.forEach((marker) => marker.setMap(null))
  interestMarkers = []

  // 일반 마커 생성
  positions.forEach((position, index) => {
    createMarker(position, index, false)
  })

  // 관심 마커 재생성
  if (interestMarkerPositions.value.length > 0) {
    interestMarkerPositions.value.forEach((position, index) => {
      createMarker(position, index, true)
    })
  }

  var level = map.getLevel()
  if (level >= 8) {
    clusterer.addMarkers(markers.concat(interestMarkers))
  } else {
    clusterer.clear()
  }
}

// 위치로 이동하는 메서드 수정
function moveToPosition(position) {
  if (!map || !position) return

  const moveLatLng = new window.kakao.maps.LatLng(position.lat, position.lng)

  // 지도 이동
  map.panTo(moveLatLng)
}

// 인포윈도우 표시 메서드 수정
function showInfoWindow(pos) {
  if (!map || !pos) return

  if (infowindow) {
    infowindow.close()
  }

  // 일반 마커와 관심 마커 모두에서 찾기
  let house = null
  let isInterest = false

  // 먼저 관심 마커에서 찾기
  const interestHouse = interestStore.interests.find(
    (interest) =>
      Math.abs(parseFloat(interest.latitude) - pos.lat) < 0.0000001 &&
      Math.abs(parseFloat(interest.longitude) - pos.lng) < 0.0000001,
  )

  if (interestHouse) {
    house = interestHouse
    isInterest = true
  } else {
    // 일반 마커에서 찾기
    const markerIndex = markerPositions.value.findIndex(
      (position) =>
        Math.abs(position[0] - pos.lat) < 0.0000001 && Math.abs(position[1] - pos.lng) < 0.0000001,
    )
    if (markerIndex !== -1) {
      house = houses.value[markerIndex]
    }
  }

  if (house) {
    const level = map.getLevel()
    const markerPosition = new window.kakao.maps.LatLng(
      isInterest ? parseFloat(house.latitude) : pos.lat,
      isInterest ? parseFloat(house.longitude) : pos.lng,
    )

    const recentDeal = isInterest
      ? interestStore.interestDetails[house.aptSeq]
      : houseStore.aptDeals?.[0] || {}

    const content = `
      <div style="padding: 12px; width: 250px; font-family: 'Spoqa Han Sans Neo', sans-serif;">
        <div style="
          border-bottom: 2px solid ${isInterest ? '#ef4444' : '#93c5fd'};
          margin-bottom: 10px;
          padding-bottom: 6px;
        ">
          <h4 style="
            margin: 0 0 3px 0;
            font-size: 15px;
            font-weight: 600;
            color: ${isInterest ? '#ef4444' : '#3b82f6'};
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
              color: ${isInterest ? '#ef4444' : '#3b82f6'};
            ">${recentDeal.tradeAmount ? formatPrice(recentDeal.tradeAmount) : '정보없음'}</span>
          </div>
        </div>
      </div>
    `

    infowindow = new window.kakao.maps.InfoWindow({
      position: markerPosition,
      content: content,
      zIndex: 3,
    })

    // 레벨에 따라 다른 방식으로 인포윈도우 표시
    if (level < 8) {
      infowindow.setMap(map)
    } else {
      // 클러스터 모드에서는 마커를 찾아서 표시
      const targetMarkers = isInterest ? interestMarkers : markers
      const marker = targetMarkers.find((m) => {
        const mPos = m.getPosition()
        return (
          Math.abs(mPos.getLat() - markerPosition.getLat()) < 0.0000001 &&
          Math.abs(mPos.getLng() - markerPosition.getLng()) < 0.0000001
        )
      })
      if (marker) {
        infowindow.open(map, marker)
      }
    }
  }
}

// 가격 포맷팅 함수 수정
function formatPrice(price) {
  if (!price || price === '?') return '?'
  const amount = parseInt(price.toString().replace(/,/g, ''))

  // 1천만원 미만인 경우 (예: 9,800 -> 9,800만)
  if (amount < 1000) {
    return `${amount}만`
  }

  // 1천만원 이상, 1억 미만인 경우 (예: 8,500 -> 8,500만, 9,900 -> 9,900만)
  if (amount < 10000) {
    // 천만원 단위가 있는 경우
    if (amount >= 1000) {
      const chun = Math.floor(amount / 1000)
      const rest = amount % 1000
      return rest > 0 ? `${chun},${rest.toString().padStart(3, '0')}만` : `${chun}천만`
    }
    return `${amount}만`
  }

  // 1억 이상인 경우 (기존 로직)
  const uk = Math.floor(amount / 10000)
  const rest = amount % 10000

  // 나머지가 있는 경우 (예: 1억 5000만원)
  if (rest > 0) {
    const decimal = (rest / 10000).toFixed(1)
    return `${uk + parseFloat(decimal)}억`
  }

  // 정확히 n억인 경우
  return `${uk}억`
}

const showInfo = (house, index) => {
  if (!house) return

  // 실거래 정보 가져오기 - interestStore에서 직접 가져옴
  const dealInfo = house ? interestStore.interestDetails[house.aptSeq] : null
  const houseDetail = dealInfo || houseStore.houseDetails[index]

  // 관심 등록 여부 확인
  const isInterest = house.isInterest || false

  // 인포윈도우에 표시할 정보 설정
  const infoContent = {
    aptName: house.aptName,
    legalDong: house.legalDong,
    roadName: house.roadName || '',
    roadNameBonbun: house.roadNameBonbun || '',
    roadNameBubun: house.roadNameBubun || '',
    buildYear: house.buildYear || '',
    jibun: house.jibun || '',
    dong: house.dong || '',
    isInterest: isInterest, // 관심 등록 여부 추가
    recentPrice: houseDetail?.recentPrice || '정보없음',
    dealYear: houseDetail?.dealYear || '',
    dealMonth: houseDetail?.dealMonth || '',
    area: houseDetail?.area || '',
    floor: houseDetail?.floor || '',
  }

  // 인포윈도우 표시
  showInfoWindow.value = true
  selectedHouse.value = infoContent
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
