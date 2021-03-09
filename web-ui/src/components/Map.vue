<template>
  <div id="container" class="h300 w300" style="position: relative;width: 100%;height:500px;">

  </div>
</template>

<script>
import MapLoader from '@/assets/AMap.js'
let map
export default {
  name: "Map",
  data () {
    return {
      map: null,
      thisPosition: {
        location: '',
        lng: '',
        lat: ''
      },
    }
  },
  mounted: function () {
    this.init()
  },
  methods: {
    init: function () {
      MapLoader().then(AMap => {
        map = new AMap.Map('container', {
          center: [117.117, 29.27],//需求的城市的经度和 纬度
          resizeEnable: true,
          zoom: 10
        })
        AMap.plugin(['AMap.ToolBar', 'AMap.Scale'], function () {
          map.addControl(new AMap.ToolBar())
          map.addControl(new AMap.Scale())
        })
      })
    },
  }
}
</script>

<style scoped>

</style>
