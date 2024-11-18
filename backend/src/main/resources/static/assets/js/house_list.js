document.addEventListener("DOMContentLoaded", function() {
	fetchSidoData();
});

function fetchSidoData() {
	fetch(`${root}/house/location?type=sido`)
		.then(response => response.json())
		.then(data => {
			console.log("하이하이");
			console.log(data);
			const sidoSelect = document.querySelector("#sido");
			sidoSelect.innerHTML = `<option value="">시도선택</option>`;

			data.forEach(sido => {
				const option = document.createElement('option');
				option.value = sido;
				option.textContent = sido;
				sidoSelect.appendChild(option);
			});
		})
		.catch(error => console.error('Error fetching sido data:', error));
}

document.querySelector("#sido").addEventListener("change", function() {
	const sidoCode = this.value;
	fetch(`${root}/house/location?type=gugun&sido=` + sidoCode)
		.then(response => response.json())
		.then(data => {
			const gugunSelect = document.querySelector("#gugun");
			gugunSelect.innerHTML = '';

			const defaultOption = document.createElement('option');
			defaultOption.value = "";
			defaultOption.textContent = "구군선택";
			gugunSelect.appendChild(defaultOption);

			data.forEach(gugun => {
				if (gugun == null) return;
				const option = document.createElement('option');
				option.value = gugun;
				option.textContent = gugun;
				gugunSelect.appendChild(option);
			});
		});
});

document.querySelector("#gugun").addEventListener("change", function() {
	const sidoCode = document.querySelector("#sido").value;
	const gugunCode = this.value;
	fetch(`${root}/house/location?type=dong&sido=` + sidoCode + `&gugun=` + gugunCode)
		.then(response => response.json())
		.then(data => {
			const dongSelect = document.querySelector("#dong");
			dongSelect.innerHTML = '';

			const defaultOption = document.createElement('option');
			defaultOption.value = "";
			defaultOption.textContent = "동선택";
			dongSelect.appendChild(defaultOption);

			data.forEach(dong => {
				if (dong == null) return;
				const option = document.createElement('option');
				option.value = dong;
				option.textContent = dong;
				dongSelect.appendChild(option);
			});
		});
});

document.querySelector("#dong").addEventListener("change", function() {
	const sidoCode = document.querySelector("#sido").value;
	const gugunCode = document.querySelector("#gugun").value;
	const dongCode = this.value;

	fetch(`${root}/house/dealYears?sido=` + sidoCode + `&gugun=` + gugunCode + `&dong=` + dongCode)
		.then(response => response.json())
		.then(data => {
			const yearSelect = document.querySelector("#year");
			yearSelect.innerHTML = '';

			const defaultOption = document.createElement('option');
			defaultOption.value = "";
			defaultOption.textContent = "매매년도선택";
			yearSelect.appendChild(defaultOption);

			data.forEach(year => {
				if (year == null) return;
				const option = document.createElement('option');
				option.value = year;
				option.textContent = year + "년";
				yearSelect.appendChild(option);
			});
		});

	const yearSelect = document.querySelector("#month");
	yearSelect.innerHTML = '';

	const defaultOption = document.createElement('option');
	defaultOption.value = "";
	defaultOption.textContent = "매매월선택";
	yearSelect.appendChild(defaultOption);
});

document.querySelector("#year").addEventListener("change", function() {
	const sidoCode = document.querySelector("#sido").value;
	const gugunCode = document.querySelector("#gugun").value;
	const dongCode = document.querySelector("#dong").value;
	const year = this.value;

	fetch(`${root}/house/dealMonths?sido=` + sidoCode + `&gugun=` + gugunCode + `&dong=` + dongCode + `&year=` + year)
		.then(response => response.json())
		.then(data => {
			const monthSelect = document.querySelector("#month");
			monthSelect.innerHTML = '';

			const defaultOption = document.createElement('option');
			defaultOption.value = "";
			defaultOption.textContent = "매매월선택";
			monthSelect.appendChild(defaultOption);

			data.forEach(month => {
				if (month == null) return;
				const option = document.createElement('option');
				option.value = month.toString().padStart(2, '0'); // 월이 1~12일 경우 두 자리 문자열로 변환
				option.textContent = month + "월"; // "1월", "2월" 형식으로 표시
				monthSelect.appendChild(option);
			});
		});
});

document.querySelector("#list-btn").addEventListener("click", function() {
	const sidoCode = document.querySelector("#sido").value;
	const gugunCode = document.querySelector("#gugun").value;
	const dongCode = document.querySelector("#dong").value;
	const month = document.querySelector("#month").value;

	if (!sidoCode || !gugunCode || !dongCode) {
		alert("모든 필드를 선택해 주세요!");
		return;
	}

	fetch(`${root}/house/houseDealList?sido=` + sidoCode + `&gugun=` + gugunCode + `&dong=` + dongCode + `&month=` + month)
		.then(response => response.json())
		.then(data => {
			console.log("조회된 리스트 데이터:", data);
			const aptlistContainer = document.querySelector("#aptlist");
			aptlistContainer.innerHTML = '';

			data.forEach(apt => {
				const row = document.createElement('tr');
				const nameCell = document.createElement('td');
				nameCell.textContent = apt.aptName || '-';

				const floorCell = document.createElement('td');
				floorCell.textContent = apt.floor || '-';

				const areaCell = document.createElement('td');
				areaCell.textContent = apt.excluUseAr ? apt.excluUseAr.toFixed(2) : '-';

				const dongCell = document.createElement('td');
				dongCell.textContent = apt.legalDong || '-';

				const amountCell = document.createElement('td');
				amountCell.textContent = apt.tradeAmount || '-';

				row.appendChild(nameCell);
				row.appendChild(floorCell);
				row.appendChild(areaCell);
				row.appendChild(dongCell);
				row.appendChild(amountCell);

				// 클릭 시 aptSeq를 가지고 요청 보내기
				row.addEventListener("click", function() {
					const aptSeq = apt.aptSeq; // aptSeq 가져오기
					fetch(`${root}/house/${aptSeq}`) // aptSeq로 요청 보내기
						.then(response => {
							if (!response.ok) {
								throw new Error('Network response was not ok');
							}
							return response.json(); // JSON으로 변환
						})
						.then(data => {
							console.log("아파트 상세 데이터:", data);
							viewMap(data.latitude, data.longitude, data.aptNm);
						})
						.catch(error => console.error('Error fetching apartment data:', error));
				});

				aptlistContainer.appendChild(row);
			});



			document.querySelector("#apt-table-container").style.display = "block";
		})
		.catch(error => console.error('Error fetching apartment data:', error));
});
