<template>
  <div
    class="house-list-item"
    @click="selectHouse"
    @mouseover="colorChange(true)"
    @mouseout="colorChange(false)"
    :class="{ 'mouse-over-bgcolor': isColor }"
  >
    <div class="apt-icon-div">
      <b-icon-house-door-fill class="apt-icon" />
    </div>
    <div class="apt-info-div">
      <div class="apt-name">[{{ house.legalDong }}] {{ house.aptName }}</div>
    </div>
  </div>
</template>

<script>
import { useHouseStore } from '@/stores/houseStore'

export default {
  name: 'HouseListItem',
  data() {
    return {
      isColor: false,
      houseinfo: {
        roadName: '',
        roadNameBonbun: '',
        roadNameBubun: '',
      },
    }
  },
  props: {
    house: Object,
  },
  setup() {
    const houseStore = useHouseStore()
    return {
      houseStore,
    }
  },
  computed: {
    none() {
      return this.houseStore.none
    },
  },
  methods: {
    selectHouse() {
      console.log('Selected house:', this.house)

      this.houseStore.setSelectedHouse(this.house)

      if (this.house.latitude && this.house.longitude) {
        this.houseStore.setSelectedPosition({
          lat: parseFloat(this.house.latitude),
          lng: parseFloat(this.house.longitude),
        })
      }

      if (this.house.roadName) {
        this.houseinfo.roadName = this.house.roadName

        if (this.house.roadNameBonbun) {
          this.houseinfo.roadNameBonbun = Number(this.house.roadNameBonbun).toString()
        }

        if (this.house.roadNameBubun) {
          this.houseinfo.roadNameBubun = Number(this.house.roadNameBubun).toString()
        }
      }
    },
    colorChange(flag) {
      this.isColor = flag
    },
  },
}
</script>

<style>
/* Spoqa Han Sans Neo 폰트 적용 */
* {
  font-family: 'Spoqa Han Sans Neo', 'sans-serif';
  font-weight: normal;
}

.house-list-item {
  display: flex;
  align-items: center;
  padding: 16px 20px; /* 패딩을 상하 16px, 좌우 20px로 증가 */
  border-bottom: 1px solid #f0f0f0; /* 아이템 사이 구분선 */
  cursor: pointer;
}

.house-list-item:last-child {
  border-bottom: none; /* 마지막 아이템은 구분선 제거 */
}

.apt-icon-div {
  margin-right: 16px; /* 오른쪽 마진만 지정 */
}

.apt-icon {
  font-size: 25px;
  color: #2196f3;
}

.apt-info-div {
  flex: 1;
}

.apt-name {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  font-size: 16px;
}

.mouse-over-bgcolor {
  background-color: #f5f5f5; /* 호버 색상을 좀 더 연하게 변경 */
}
</style>
