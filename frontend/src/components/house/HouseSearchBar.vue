<template>
  <div>
    <div class="map-select-div">
      <select class="form-control" v-model="sido" @change="gugunList">
        <option value="all">시/도</option>
        <option v-for="(sido, index) in sidos" :key="index" :value="sido">
          {{ sido }}
        </option>
      </select>
      <select class="form-control" v-model="gugun" @change="dongList">
        <option value="all">구/군</option>
        <option v-for="(gugun, index) in guguns" :key="index" :value="gugun">
          {{ gugun }}
        </option>
      </select>
      <select class="form-control" v-model="dong" @change="houseList">
        <option value="all">동</option>
        <option v-for="(dong, index) in dongs" :key="index" :value="dong">
          {{ dong }}
        </option>
      </select>
    </div>
    <div class="map-name-div">
      <div>
        <input
          type="text"
          class="form-control name-search"
          placeholder="아파트명"
          style="width: 256px"
          v-model="name"
        />
      </div>
      <div>
        <button class="btn btn-primary" style="margin: 0" @click="nameList">검색</button>
      </div>
    </div>
  </div>
</template>

<script>
import { useAddressStore } from '@/stores/addressStore' // Pinia store import
// import { BUS } from '@/store/modules/EventBus' // EventBus for communication

export default {
  name: 'HouseSearchBar',
  data() {
    return {
      sido: 'all',
      gugun: 'all',
      dong: 'all',
      name: '',
    }
  },
  created() {
    // this.addressStore.getSido() // 시/도 목록 가져오기
  },
  computed: {
    // Pinia store instance 한번만 호출해서 재사용
    addressStore() {
      return useAddressStore() // store instance를 computed로 가져와서 재사용
    },
    // computed로 store의 상태를 사용
    sidos() {
      return this.addressStore.sidos
    },
    guguns() {
      return this.addressStore.guguns
    },
    dongs() {
      return this.addressStore.dongs
    },
  },
  methods: {
    gugunList() {
      this.addressStore.clearAddressData() // 구군 목록 초기화
      this.gugun = 'all'
      if (this.sido !== 'all') {
        this.addressStore.getGugun(this.sido) // 시도에 해당하는 구군을 가져옴
      }
    },
    dongList() {
      this.addressStore.clearAddressData() // 동 목록 초기화
      this.dong = 'all'
      if (this.sido !== 'all' && this.gugun !== 'all') {
        const params = {
          sidoName: this.sido,
          gugunName: this.gugun,
        }
        this.addressStore.getDong(params) // 구군에 해당하는 동을 가져옴
      }
    },
    houseList() {
      this.addressStore.clearAddressData() // 집 목록 초기화
      if (this.sido !== 'all' && this.gugun !== 'all' && this.dong !== 'all') {
        this.getHouseListByAddress({
          sido: this.sido,
          gugun: this.gugun,
          dong: this.dong,
        })
      }
      // BUS.$emit('moveDong', `${this.gugun} ${this.dong}`) // 동 이동 이벤트 전송
    },
    nameList() {
      this.addressStore.clearAddressData() // 집 목록 초기화
      if (this.name) {
        this.getNameList(this.name) // 아파트 이름으로 검색
      }
    },
  },
}
</script>

<style>
.map-select-div {
  display: flex;
  justify-content: center;
}

.map-name-div {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  margin-top: 17px;
}
</style>
