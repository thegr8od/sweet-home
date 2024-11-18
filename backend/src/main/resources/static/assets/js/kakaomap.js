var markers = []; // 모든 마커를 저장할 배열
var map;

function renderMap() {
	// HTML5의 Geolocation API를 사용하여 현재 위치 얻기
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(
			function(position) {
				let lat = position.coords.latitude; // 위도
				let lng = position.coords.longitude; // 경도

				// 카카오지도
				let latlng = { lat: lat, lng: lng };
				var mapContainer = document.getElementById("map"), // 지도를 표시할 div
					mapOption = {
						center: new kakao.maps.LatLng(latlng.lat, latlng.lng), // 지도의 중심좌표
						level: 3, // 지도의 확대 레벨
					};

				// 지도를 표시할 div와 지도 옵션으로 지도를 생성합니다
				map = new kakao.maps.Map(mapContainer, mapOption);

				// 지도에 컨트롤 올리기
				var mapTypeControl = new kakao.maps.MapTypeControl();
				map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT); //TOPRIGHT는 오른쪽 위
				var zoomControl = new kakao.maps.ZoomControl();
				map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);

				// 현재 위치에 마커 생성
				let markerPosition = new kakao.maps.LatLng(latlng.lat, latlng.lng); // 마커가 표시될 위치입니다
				let marker = createMarker(markerPosition); // 마커 생성 함수 호출

				marker.setMap(map);
				markers.push(marker); // 생성된 마커를 배열에 저장
				map.setCenter(coords);
			},
			function(error) {
				console.error("현재 위치를 가져오는데 실패했습니다: " + error.message);
			}
		);
	} else {
		console.error("Geolocation API를 지원하지 않는 브라우저입니다.");
	}
}

var infowindow;
function viewMap(lat, lng, apt) {
	if (map) {
		// 새로운 위치에 마커 생성
		let coords = new kakao.maps.LatLng(lat, lng);
		let marker = createMarker(coords); // 마커 생성 함수 호출

		marker.setMap(map);
		markers.push(marker); // 생성된 마커를 배열에 저장

		// 인포윈도우로 장소에 대한 설명을 표시합니다
		infowindow = new kakao.maps.InfoWindow({
			content: `<div style="width:150px;text-align:center;padding:6px 0;">${apt}</div>`,
			removable: true,
		});
		infowindow.open(map, marker);

		// 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
		map.setCenter(coords);
	} else {
		console.error("지도 객체가 초기화되지 않았습니다.");
	}
}

// 마커를 생성하는 함수
function createMarker(position) {
	var imageSrc = "/assets/img/geo-map-pin.png"; // 마커이미지의 주소입니다
	var imageSize = new kakao.maps.Size(60, 66); // 마커이미지의 크기입니다
	var imageOption = { offset: new kakao.maps.Point(27, 69) }; // 마커이미지의 옵션입니다

	var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);

	// 마커를 생성하여 반환합니다
	return new kakao.maps.Marker({
		position: position,
		image: markerImage, // 마커이미지 설정
	});
}

// 페이지가 로드될 때 지도 초기화
renderMap();
