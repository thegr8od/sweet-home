<template>
  <div>
    <table class="table table-bordered table-hover">
      <thead></thead>
      <tbody>
        <!-- <tr >
          <td class="th">사용승인일</td>
          <td>{{ detail1.kaptUsedate }}</td>
        </tr> -->
        <tr>
          <td class="th">대장 전용면접합계</td>
          <td>{{ detail1.privArea }}</td>
        </tr>
        <tr>
          <td class="th">건물구조</td>
          <td>{{ detail2.codeStr }}</td>
        </tr>
        <tr>
          <td class="th">복도유형</td>
          <td>{{ detail1.codeHallNm }}</td>
        </tr>

        <tr>
          <td class="th">동수</td>
          <td>{{ detail1.kaptDongCnt }}</td>
        </tr>
        <tr>
          <td class="th">호수</td>
          <td>{{ detail1.hoCnt }}</td>
        </tr>
        <tr>
          <td class="th">세대수</td>
          <td>{{ detail1.kaptdaCnt }}</td>
        </tr>
        <tr>
          <td class="th">난방방식</td>
          <td>{{ detail1.codeHeatNm }}</td>
        </tr>
        <tr>
          <td class="th">급수방식</td>
          <td>{{ detail2.codeWsupply }}</td>
        </tr>
        <tr>
          <td class="th">수전용량</td>
          <td>{{ detail2.kaptdEcapa }}</td>
        </tr>
        <tr>
          <td class="th">주차대수(지상/지하)</td>
          <td>{{ detail2.kaptdPcnt }} / {{ detail2.kaptdPcntu }}</td>
        </tr>
        <tr>
          <td class="th">승강기대수</td>
          <td>{{ detail2.kaptdEcnt }}</td>
        </tr>

        <tr>
          <td class="th" colspan="2">전용면적별 세대현황</td>
        </tr>
        <tr>
          <td class="th text-end">60㎡ ~ 85㎡</td>
          <td>{{ detail1.kaptMparea_85 }}</td>
        </tr>
        <tr>
          <td class="th text-end">85㎡ ~ 135㎡</td>
          <td>{{ detail1.kaptMparea_135 }}</td>
        </tr>
        <tr>
          <td class="th text-end">135㎡~</td>
          <td>{{ detail1.kaptMparea_136 }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import { useHouseStore } from '@/stores/houseStore'

export default {
  name: 'HouseInfo',
  setup() {
    // Pinia store 가져오기
    const houseStore = useHouseStore()

    return {
      detail1: houseStore.detail1, // 상태 detail1 가져오기
      detail2: houseStore.detail2, // 상태 detail2 가져오기
    }
  },
}
</script>

<style>
.th {
  font-weight: bold;
}
.text-end {
  text-align: end;
}
</style>
