<!-- PopulationTable.vue -->
<template>
  <table class="min-w-full divide-y divide-gray-200">
    <thead class="bg-gray-50">
      <tr>
        <th class="px-4 py-2">통</th>
        <th class="px-4 py-2">반</th>
        <th class="px-4 py-2">총인구</th>
        <th class="px-4 py-2">세대수</th>
        <th class="px-4 py-2">남성</th>
        <th class="px-4 py-2">여성</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="item in data" :key="`${item.tong}-${item.ban}-${item.statsYm}`">
        <td class="px-4 py-2">{{ item.tong }}통</td>
        <td class="px-4 py-2">{{ item.ban }}반</td>
        <td class="px-4 py-2">{{ item.totalPopulation }}명</td>
        <td class="px-4 py-2">{{ item.households }}세대</td>
        <td class="px-4 py-2">{{ item.male }}명</td>
        <td class="px-4 py-2">{{ item.female }}명</td>
      </tr>
    </tbody>
  </table>
</template>

<script setup>
const props = defineProps({
  data: {
    type: Array,
    required: true,
  },
})
</script>
