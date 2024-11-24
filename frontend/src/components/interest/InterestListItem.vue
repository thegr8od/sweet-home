<template>
  <div
    class="interest-list-item"
    @click="selectHouse"
    @mouseover="colorChange(true)"
    @mouseout="colorChange(false)"
    :class="{ 'mouse-over-bgcolor': isColor }"
  >
    <div class="apt-info-div">
      <div class="apt-name">[{{ interest.legalDong }}] {{ interest.aptName }}</div>
    </div>
  </div>
</template>

<script>
import { useHouseStore } from '@/stores/houseStore'

export default {
  name: 'InterestListItem',
  data() {
    return {
      isColor: false,
    }
  },
  props: {
    interest: Object,
  },
  setup() {
    const houseStore = useHouseStore()
    return {
      houseStore,
    }
  },
  methods: {
    async selectHouse() {
      if (!this.interest.aptSeq) return

      // 선택된 아파트의 위치 정보가 있다면 지도에 표시
      if (this.interest.latitude && this.interest.longitude) {
        const position = {
          lat: parseFloat(this.interest.latitude),
          lng: parseFloat(this.interest.longitude),
        }
        this.houseStore.setSelectedPosition(position)
      }

      // 선택된 아파트 정보 설정
      const selectedHouse = {
        aptSeq: this.interest.aptSeq,
        aptName: this.interest.aptName,
        legalDong: this.interest.legalDong,
        latitude: this.interest.latitude,
        longitude: this.interest.longitude,
      }
      
      // houseStore의 상태 업데이트
      this.houseStore.setSelectedHouse(selectedHouse)

      // 아파트 상세 정보 가져오기
      await this.houseStore.getDetail({ aptSeq: this.interest.aptSeq })
    },
    colorChange(flag) {
      this.isColor = flag
    },
  },
}
</script>

<style scoped>
.interest-list-item {
  display: flex;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
}

.interest-list-item:last-child {
  border-bottom: none;
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
  background-color: #f5f5f5;
}
</style> 