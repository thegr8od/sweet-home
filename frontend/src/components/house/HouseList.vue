<template>
  <div v-if="houses && houses.length != 0">
    <house-list-item v-for="(house, index) in houses" :key="index" :house="house" />
  </div>
  <div v-else>
    <h4>주택 목록이 없습니다.</h4>
  </div>
</template>

<script>
import HouseListItem from '@/components/house/HouseListItem.vue'
import { useHouseStore } from '@/stores/houseStore' // Pinia store import

export default {
  name: 'HouseList',
  components: {
    HouseListItem,
  },
  computed: {
    // Pinia store를 computed로 사용하여 상태를 가져옴
    houses() {
      const houseStore = useHouseStore() // Pinia store instance 가져오기
      return houseStore.houses // store에서 houses 상태 가져오기
    },
  },
}
</script>

<style></style>
