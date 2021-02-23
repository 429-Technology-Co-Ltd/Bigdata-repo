package qm.qiaomeng.model

/**
 * @ClassName: TBItem
 * @Description: TODO
 * @Create by: LinYoung
 * @Date: 2021/2/13 17:38
 */
case class TBItem(
                   source: String,
                   itemId: Long,
                   itemDescription: String,
                   itemTitle: String,
                   itemShortTitle: String,
                   pictureURL: String,
                   smallImages: Array[String],
                   price: Double,
                   categoryId: Long,
                   categoryName: String,
                   categoryId1: Long,
                   categoryName1: String,
                   postFee: String,
                   MKT: String,
                   whiteImage: String,
                   tkTotalSales: Int,
                   DXJHInfo: String,
                   DXJH: String,
                   commissionType: String,
                   commissionRate: String,
                   volume: Int,
                   itemURL: String,
                   City: String,
                   couponId: String,
                   couponInfo: String,
                   couponTotalCount: Int,
                   couponRemainCount: Double,
                   zkFinalPrice: Double,
                   presaleDeposit: String,
                   presaleStartTime: Long,
                   presaleEndTime: Long,
                   presaleTailStartTime: Long,
                   presaleTailEndTime: Long,
                   shopTitle: String,
                   sellerId: Long,
                   userType: String,
                   DSR: String,
                   Nick: String,
                   xId: String,
                   URL: String,
                   superiorBrand: Int,
                   ctime: String
                 )
