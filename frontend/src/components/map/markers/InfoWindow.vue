<template>
  <div class="bg-white rounded-lg shadow-lg w-64">
    <!-- Header -->
    <div class="p-4 border-b">
      <div class="flex justify-between items-center mb-2">
        <h3 class="text-lg font-bold">{{ title }}</h3>
        <button @click="$emit('close')" class="text-gray-500 hover:text-gray-700">
          <XIcon class="w-5 h-5" />
        </button>
      </div>
      <div class="text-sm text-gray-600">{{ area }}㎡ - {{ area2 }}㎡</div>
    </div>

    <!-- Content -->
    <div class="p-4">
      <div class="bg-gray-100 rounded-md p-3">
        <div class="grid grid-cols-2 gap-4">
          <div>
            <div class="text-sm text-gray-500 mb-1">매매</div>
            <div class="font-medium">{{ sales }}억</div>
            <div class="text-xs text-gray-400">{{ salesDate }}</div>
          </div>
          <div>
            <div class="text-sm text-gray-500 mb-1">전세</div>
            <div class="font-medium">{{ lease }}억</div>
            <div class="text-xs text-gray-400">{{ leaseDate }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { XIcon } from 'lucide-vue-next'

defineProps({
  title: {
    type: String,
    required: true,
  },
  area: {
    type: Number,
    required: true,
  },
  area2: {
    type: Number,
    required: true,
  },
  sales: {
    type: Number,
    required: true,
  },
  salesDate: {
    type: String,
    required: true,
  },
  lease: {
    type: Number,
    required: true,
  },
  leaseDate: {
    type: String,
    required: true,
  },
})

defineEmits(['close'])
</script>
