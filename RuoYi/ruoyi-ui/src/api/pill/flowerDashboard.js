import request from '@/api/request'

// 获取统计数据
export function getStatistics() {
  return request({
    url: '/pill/flowerDashboard/statistics',
    method: 'get'
  })
}


