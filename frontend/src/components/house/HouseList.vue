<template>
  <div class="house-list-container">
    <div v-if="houses && houses.length != 0">
      <house-list-item v-for="(house, index) in houses" :key="index" :house="house" />
    </div>
    <div v-else class="no-houses">
      <h4>주택 목록이 없습니다.</h4>
    </div>
  </div>
</template>

<script>
import HouseListItem from '@/components/house/HouseListItem.vue'
import { useHouseStore } from '@/stores/houseStore'

export default {
  name: 'HouseList',
  components: {
    HouseListItem,
  },
  computed: {
    houses() {
      const houseStore = useHouseStore()
      return houseStore.houses
    },
  },
}
</script>

<style scoped>
.house-list-container {
  padding: 8px; /* 리스트 전체 패딩 축소 */
}

.no-houses {
  padding: 16px;
  text-align: center;
  color: #666;
}
</style>
