<template>
  <div>
    <!-- <deal-chart /> -->
    <div
      v-for="(deal, index) in deals"
      :key="index"
      class="dealList"
      @mouseover="colorChange(true, index)"
      @mouseout="colorChange(false, index)"
      :class="{ 'mouse-over-bgcolor': columnHoverCheck(index) }"
    >
      <h4 style="color: blue">매매 {{ deal.dealAmount }} 만원</h4>
      <div>{{ deal.floor }}층 | {{ deal.area }}㎡</div>
      <div>거래일자 : {{ deal.dealYear }} / {{ deal.dealMonth }} / {{ deal.dealDay }}</div>
    </div>
  </div>
</template>

<script>
// import DealChart from '@/components/Chart/DealChart.vue'
import { useHouseStore } from '@/stores/houseStore'

export default {
  name: 'HouseInfo',
  data() {
    return {
      overColumn: null,
    }
  },
  components: {
    // DealChart,
  },
  setup() {
    // Pinia store 가져오기
    const houseStore = useHouseStore()

    return {
      deals: houseStore.deals, // deals 상태 사용
      setNoneFalse: houseStore.setNoneFalse, // setNoneFalse 액션 사용
    }
  },
  methods: {
    colorChange(flag, index) {
      this.overColumn = flag ? index : null
    },
    columnHoverCheck(index) {
      return index === this.overColumn
    },
    dispalynone() {
      this.setNoneFalse(true) // houseStore의 액션 호출
    },
  },
}
</script>

<style>
.mouse-over-bgcolor {
  background-color: rgb(214, 211, 211);
}

.detail-row {
  font-size: 19px;
  font-weight: bold;
  padding-top: 10px;
}

.dealList {
  border-top: 2px solid #aeb4bf;
  width: 323px;
  padding: 14px;
}
</style>
