<template>
  <div
    class="m-2"
    @click="selectHouse"
    @mouseover="colorChange(true)"
    @mouseout="colorChange(false)"
    :class="{ 'mouse-over-bgcolor': isColor }"
    style="display: flex; align-items: center"
  >
    <div class="apt-icon-div">
      <b-icon-house-door-fill class="apt-icon" />
    </div>
    <div class="apt-info-div">
      <div style="text-overflow: ellipsis">[{{ house.legalDong }}] {{ house.aptName }}</div>
    </div>
  </div>
</template>

<script>
import { useHouseStore } from '@/stores/houseStore'
// import { BUS } from '@/store/modules/EventBus'

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
      //   // house 위치 변경 이벤트
      //   BUS.$emit('change-hposition', {
      //     lat: this.house.lat,
      //     lng: this.house.lng,
      //   })

      // house deals가 바뀔 때 마다 새로운 detail창 불러오기
      this.houseStore.setNoneFalse(true)
      this.houseStore.detailHouse(this.house)

      // houseinfo 업데이트
      this.houseinfo.roadName = this.house.roadName

      let roadNameBonbun = this.house.roadNameBonbun
      roadNameBonbun = Number(roadNameBonbun).toString()
      this.houseinfo.roadNameBonbun = roadNameBonbun

      let roadNameBubun = this.house.roadNameBubun
      roadNameBubun = Number(roadNameBubun).toString()
      this.houseinfo.roadNameBubun = roadNameBubun

      // 디테일 정보 가져오기
      this.houseStore.getDetail(this.houseinfo)
    },
    colorChange(flag) {
      this.isColor = flag
    },
  },
}
</script>

<style>
.apt {
  width: 50px;
}

.apt-info-div div {
  white-space: nowrap;
  width: 278px;
  overflow: hidden;
  text-overflow: ellipsis;
  font-weight: bold;
  font-size: 16px;
}
.mouse-over-bgcolor {
  background-color: rgb(214, 211, 211);
}
.apt-icon-div {
  margin: 8px 21px 8px 15px;
}
.apt-icon {
  font-size: 25px;
  color: #2196f3;
}
</style>
