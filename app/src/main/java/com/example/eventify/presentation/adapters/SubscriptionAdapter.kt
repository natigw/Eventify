package com.example.eventify.presentation.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.example.common.base.BaseAdapter
import com.example.eventify.databinding.SampleSubscriptionBinding
import com.example.domain.model.subscription.SubscriptionData

class SubscriptionAdapter(
//    val context: Context,
    val currentPackage: String,
    var isAnnualBilling: Boolean,
    val onClick: (packageName: String) -> Unit  //TODO -> bundan emin olmadin
) : BaseAdapter<SampleSubscriptionBinding>(SampleSubscriptionBinding::inflate) {

    private var packages = arrayOf(   //TODO -> bu basqa layerde olmalidi? static ve inapp bir seydi
        SubscriptionData(
            name = "Base",
            features = "Access to cultural events list\n\nCommunity contributions",    //[Feature1, Feature2, ..]
            priceMonthly = 0.0,
            priceAnnually = 0.0
        ),
        SubscriptionData(
            name = "Pro",
            features = "Access to full map functionalities\n\nCommunity Contributions\n\nExclusive Deals\n\nPriority Support",
            priceMonthly = 40.0,
            priceAnnually = 40.0 * 12 - 80
        ),
        SubscriptionData(
            name = "Enterprise",
            features = "Access to full map functionalities\n\nCommunity Contributions\n\nExclusive Deals and Offers\n\nPriority Support\n\nEnterprise Event Offers\n\nAdvanced Statistics",
            priceMonthly = 99.0,
            priceAnnually = 99.0 * 12 - 200
        )
    )

    override fun getItemCount(): Int {
        return packages.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindLight(binding: SampleSubscriptionBinding, position: Int) {
        val item = packages[position]
        with(binding) {
            textPackageNameSubscription.text = item.name
            textFeaturesSubscription.text = item.features
            if (item.priceMonthly == 0.0) {
                textPriceSubscription.text = "Free"
                textMonthlyAnnuallySubscription.visibility = View.GONE
                textSaveMoneySubscription.visibility = View.INVISIBLE
            }
            else {
                textPriceSubscription.text = if (isAnnualBilling) "$${item.priceAnnually}" else "$${item.priceMonthly}"
                textMonthlyAnnuallySubscription.text = if (isAnnualBilling) "/year" else "/month"
                if (isAnnualBilling) {
                    textSaveMoneySubscription.visibility = View.VISIBLE
                    textSaveMoneySubscription.text = "Save $${item.priceMonthly*12 - item.priceAnnually}"
                }
            }
            if (item.name == currentPackage) {
                buttonChoosePackageSubscription.text = "Current plan"
                buttonChoosePackageSubscription.isEnabled = false
//                buttonChoosePackageSubscription.setBackgroundColor(R.color.gray_light)  //TODO -> disable olanda gray olsun
            }
            buttonChoosePackageSubscription.setOnClickListener {
                onClick(item.name)
            }
        }
    }

//    fun updateAdapter() {
//        notifyDataSetChanged()
//    }
}