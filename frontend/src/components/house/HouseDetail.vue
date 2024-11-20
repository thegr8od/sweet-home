<template>
  <div v-if="house">
    <div class="close-div">
      <b-icon-x-lg class="close" @click="dispalynone" />
    </div>
    <div class="detail-name">
      <div class="title-area">
        <like-button v-if="isLogin" @btnClick="setLike" :isLiked="likeStatus"></like-button>
        <h3>{{ house.apartmentName }}</h3>
      </div>
      <div class="title-sub-info">
        준공 : {{ house.buildYear }} | {{ house.roadName }} ({{ house.dong }})
      </div>
    </div>
    <div class="detail-img-div">
      <img class="sell-thumbnail" :src="img_url" alt="Image 1" />
    </div>
    <div class="detail-menu-bar">
      <b-button @click="showInfo" class="menu" :class="{ active: detail_type.info }" id="info">
        매물정보
      </b-button>
      <b-button @click="showDeal" class="menu" :class="{ active: detail_type.deal }" id="deal">
        실거래가
      </b-button>
      <b-button @click="showInfra" class="menu" :class="{ active: detail_type.infra }" id="infra">
        인프라
      </b-button>
    </div>
    <detail-info v-if="detail_type.info"></detail-info>
    <detail-deal v-else-if="detail_type.deal"></detail-deal>
    <detail-infra v-else-if="detail_type.infra"></detail-infra>
  </div>
</template>

<script>
import { useHouseStore } from '@/stores/houseStore' // Pinia store import
import { useMemberStore } from '@/stores/memberStore' // Pinia store import
import LikeButton from '@/components/interest/LikeButton.vue'
import DetailInfra from '@/components/house/detail/HouseInfra.vue'
import DetailInfo from '@/components/house/detail/HouseInfo.vue'
import DetailDeal from '@/components/house/detail/HouseDeal.vue'
import { ramdomImg } from '@/api/image.js'
import { apiInstance } from '@/api/index.js'

const http = apiInstance()

export default {
  name: 'HouseDetail',
  components: {
    LikeButton,
    DetailInfra,
    DetailInfo,
    DetailDeal,
  },
  data() {
    return {
      likeStatus: false,
      img_url: '',
      detail_type: { info: true, deal: false, infra: false },
    }
  },
  computed: {
    house() {
      const houseStore = useHouseStore() // Pinia store instance 가져오기
      return houseStore.house // houseStore에서 house 상태 가져오기
    },
    isLogin() {
      const memberStore = useMemberStore() // Pinia store instance 가져오기
      return memberStore.isLogin // memberStore에서 isLogin 상태 가져오기
    },
  },
  created() {
    this.img_url = ramdomImg()
    this.checkLikeStatus()
  },
  methods: {
    checkLikeStatus() {
      const code = this.house.aptCode
      http.post(`/interest/check/likeApt`, { code, user_id: 'ssafy' }).then(({ data }) => {
        this.likeStatus = data.isLiked === 'Y'
      })
    },
    showInfo() {
      this.detail_type = { info: true, deal: false, infra: false }
    },
    showDeal() {
      this.detail_type = { info: false, deal: true, infra: false }
    },
    showInfra() {
      this.detail_type = { info: false, deal: false, infra: true }
    },
    setLike() {
      let param = { code: this.house.aptCode, user_id: 'ssafy' }

      if (this.likeStatus) {
        http
          .post(`/interest/delete/apt`, param)
          .then(() => {
            this.likeStatus = !this.likeStatus
          })
          .catch(() => {})
      } else {
        http
          .post(`/interest/apt`, param)
          .then(() => {
            this.likeStatus = !this.likeStatus
          })
          .catch(() => {})
      }
    },
    dispalynone() {
      const houseStore = useHouseStore()
      houseStore.setNoneFalse(true) // Pinia store를 사용하여 상태 변경
    },
  },
}
</script>

<style scoped>
.title-area {
  display: flex;
  align-items: center;
  justify-content: flex-start !important;
  width: 325px !important;
}
.mouse-over-bgcolor {
  background-color: rgb(214, 211, 211);
}

.close-div {
  position: relative;
  width: 323px;
  padding-top: 31px;
  right: -295px;
}

.close {
  background-color: #9ca3b1;
  width: 32px;
  height: 32px;
  color: white;
  padding: 9px;
  border-radius: 3px;
}

.detail-name {
  padding-top: 14px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.detail-img-div img {
  padding: 11px 0px 40px 0;
  width: 328px;
  height: 300px;
}
.detail-menu-bar {
  margin-bottom: 20px;
  display: flex;
  justify-content: space-around;
  width: 326px;
}

.on {
  font-weight: bold;
  color: #2196f3;
}
.table td,
.table th {
  white-space: break-spaces;
}

.detail-menu-bar .menu {
  font-weight: bold;
}
.menu.active {
  background-color: #2196f3 !important;
}
</style>
