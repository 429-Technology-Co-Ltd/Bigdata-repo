package qm.qiaomeng.model

/**
 * @ClassName: PDDItem
 * @Description: TODO
 * @Create by: LinYoung
 * @Date: 2021/2/13 17:38
 */
case class PDDItem(
                    source: String,
                    itemId: Long,
                    itemName: String,
                    itemURL: String,
                    itemThumbnailURL: String,
                    itemDesc: String,
                    itemSign: String,
                    descTxt: String,
                    servTxt: String,
                    lgstTxt: String,
                    saleCount: String,
                    labelId: Long,
                    labelName: String,
                    labelIds: String,
                    categoryIds: String,
                    shopCPS: Int,
                    shopId: Long,
                    shopName: String,
                    shopType: Int,
                    shopCouponCount: Int,
                    shopCouponStartTime: Long,
                    shopCouponEndTime: Long,
                    shopHasCoupon: String,
                    shopCouponId: Long,
                    shopCouponRemain: Int,
                    shopZkCoupon: Int,
                    shopCouponMinAmount: Int,
                    shopCouponMaxAmount: Int,
                    minNormalPrice: Int,
                    minGroupPrice: Int,
                    hasCoupon: String,
                    couponDisCount: String,
                    couponCount: Int,
                    couponRemain: Int,
                    couponStartTime: Long,
                    couponEndTime: Long,
                    couponMinAmount: Int,
                    promotionRate: Int,
                    planType: Int,
                    ZSId: Long,
                    predictPromotionRate: Long,
                    searchId: String,
                    serviceTags: String,
                    unifiedTags: String,
                    sceneAuth: String,
                    ctime: String
                  )
