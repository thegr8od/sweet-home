<template>
  <div style="width: 331px">
    <div style="padding-bottom: 20px">
      <div class="flex">
        <img src="https://ifh.cc/g/QTVsCs.png" alt="" class="subway infra-img" />
        <div>{{ detail2.subwayStation }} {{ detail2.kaptdWtimesub }}</div>
      </div>
      <div class="flex">
        <img src="https://ifh.cc/g/9QnWpy.png" alt="" class="bus infra-img" />
        <div>{{ detail2.kaptdWtimebus }}</div>
      </div>
      <div class="flex">
        <img src="https://ifh.cc/g/gdpYwB.png" alt="" class="cctv infra-img" />
        <div>{{ detail2.kaptdCccnt }}개</div>
      </div>
    </div>

    <div style="width: 360px">
      <table class="table" style="width: 90%">
        <thead></thead>
        <tbody>
          <tr>
            <td class="th"><b-icon-pin-map-fill style="margin-right: 5px" />부대, 복리시설</td>
          </tr>
          <tr>
            <td>{{ detail2.welfareFacility }}</td>
          </tr>
          <tr>
            <td class="th"><b-icon-pin-map-fill style="margin-right: 5px" />편의시설</td>
          </tr>
          <tr>
            <td>{{ detail2.convenientFacility }}</td>
          </tr>
          <tr>
            <td class="th"><b-icon-pin-map-fill style="margin-right: 5px" />교육시설</td>
          </tr>
          <tr>
            <td>{{ detail2.educationFacility }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import { useHouseStore } from '@/stores/houseStore'

export default {
  name: 'HouseInfo',
  setup() {
    const houseStore = useHouseStore()

    return {
      detail1: houseStore.detail1,
      detail2: houseStore.detail2,
    }
  },
}
</script>

<style scoped>
.table td,
.table th {
  white-space: break-spaces;
}
.th {
  font-weight: bold;
}
.text-end {
  text-align: end;
}
.img-div {
  border-radius: 65px;
  background-color: #c1dce9;
  width: 48px;
  text-align: center;
}
.infra-img {
  width: 48px;
  padding: 8px;
  margin-right: 21px;
}

.flex {
  display: flex;
  align-items: center;
}
.table td,
.table th {
  white-space: break-spaces;
}
</style>
