/*************************************************************************/
/*  GooglePlayBillingUtils.java                                                    */
/*************************************************************************/
/*                       This file is part of:                           */
/*                           GODOT ENGINE                                */
/*                      https://godotengine.org                          */
/*************************************************************************/
/* Copyright (c) 2014-present Godot Engine Contributors (see AUTHORS.md).*/
/* Copyright (c) 2007-2014 Juan Linietsky, Ariel Manzur.                 */
/*                                                                       */
/* Permission is hereby granted, free of charge, to any person obtaining */
/* a copy of this software and associated documentation files (the       */
/* "Software"), to deal in the Software without restriction, including   */
/* without limitation the rights to use, copy, modify, merge, publish,   */
/* distribute, sublicense, and/or sell copies of the Software, and to    */
/* permit persons to whom the Software is furnished to do so, subject to */
/* the following conditions:                                             */
/*                                                                       */
/* The above copyright notice and this permission notice shall be        */
/* included in all copies or substantial portions of the Software.       */
/*                                                                       */
/* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,       */
/* EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF    */
/* MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.*/
/* IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY  */
/* CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,  */
/* TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE     */
/* SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.                */
/*************************************************************************/

package org.godotengine.godot.plugin.googleplaybilling.utils;
import org.godotengine.godot.Dictionary;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.ProductDetails;
import java.util.List;
import java.util.Objects;

public class GooglePlayBillingUtils {
	public static Dictionary convertPurchaseToDictionary(Purchase purchase) {
		Dictionary dictionary = new Dictionary();
		dictionary.put("original_json", purchase.getOriginalJson());
		dictionary.put("order_id", purchase.getOrderId());
		dictionary.put("package_name", purchase.getPackageName());
		dictionary.put("purchase_state", purchase.getPurchaseState());
		dictionary.put("purchase_time", purchase.getPurchaseTime());
		dictionary.put("purchase_token", purchase.getPurchaseToken());
		dictionary.put("quantity", purchase.getQuantity());
		dictionary.put("signature", purchase.getSignature());
		// PBL v6 replaced getSkus with getProducts, for a more generalized approach to items rather than relying on skus.
		// PBL V4 replaced getSku with getSkus to support multi-sku purchases,
		// use the first entry for "sku" and generate an array for "skus"
		List<String> products = purchase.getProducts();
		dictionary.put("product", products.get(0));
		String[] productsArray = products.toArray(new String[0]);
		dictionary.put("products", productsArray);
		dictionary.put("is_acknowledged", purchase.isAcknowledged());
		dictionary.put("is_auto_renewing", purchase.isAutoRenewing());
		return dictionary;
	}

	// Here is where we want to update it to make it more intelligent; this will have to include subscription data, but we'll make it smarter in how it handles it than what was there previously.
	public static Dictionary convertProductDetailsToDictionary(ProductDetails details) {
		ProductDetails.OneTimePurchaseOfferDetails purchaseDetails = Objects.requireNonNull(details.getOneTimePurchaseOfferDetails());
		Dictionary dictionary = new Dictionary();
		dictionary.put("productId", details.getProductId());
		dictionary.put("title", details.getTitle());
		dictionary.put("description", details.getDescription());
		dictionary.put("price", purchaseDetails.getFormattedPrice());
		dictionary.put("price_currency_code", purchaseDetails.getPriceCurrencyCode());
		dictionary.put("price_amount_micros", purchaseDetails.getPriceAmountMicros());
		dictionary.put("type", details.getProductType());
		return dictionary;
	}

	public static Object[] convertPurchaseListToDictionaryObjectArray(List<Purchase> purchases) {
		Object[] purchaseDictionaries = new Object[purchases.size()];

		for (int i = 0; i < purchases.size(); i++) {
			purchaseDictionaries[i] = GooglePlayBillingUtils.convertPurchaseToDictionary(purchases.get(i));
		}

		return purchaseDictionaries;
	}

	public static Object[] convertProductDetailsListToDictionaryObjectArray(List<ProductDetails> productDetails) {
		Object[] productDetailsDictionaries = new Object[productDetails.size()];

		for (int i = 0; i < productDetails.size(); i++) {
			productDetailsDictionaries[i] = GooglePlayBillingUtils.convertProductDetailsToDictionary(productDetails.get(i));
		}

		return productDetailsDictionaries;
	}
}
