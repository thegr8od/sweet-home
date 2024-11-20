<template>
  <span class="like-btn" @click="btnClick">
    <span v-if="isLiked"
      ><b-icon icon="heart-fill" :font-scale="size" style="color: #d63384"></b-icon
    ></span>
    <span v-else><b-icon icon="heart " :font-scale="size" style="color: #d63384"></b-icon></span>
  </span>
</template>

<script>
export default {
  name: 'LikeButton',
  props: ['isLiked', 'size', 'color'],
  data() {
    return {}
  },
  mounted() {},
  methods: {
    btnClick() {
      this.$emit('btnClick')
    },
  },
}
</script>

<style>
.like-btn {
  display: flex;
  background-color: #fff;
  width: 2em;
  height: 1.5em;

  border: 1px solid #d63384;
  border-radius: 5px;
  justify-content: center;
  align-items: center;
  padding: 17px;
  margin-right: 21px;
}
</style>
