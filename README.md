<div align="center">

![Android](https://img.shields.io/badge/Android-3DDC84?style=flat-square&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=flat-square&logo=firebase&logoColor=black)

</div>

# Krishi Sangam 🌾

Krishi Sangam is an Agro Node based multilingual Android application designed to help farmers sell their produce more transparently, reduce dependency on middlemen, receive faster payments, and connect with verified bulk/community buyers.

The project focuses on farmer-first product listing, Agro Node verification, transparent pricing, multilingual accessibility, and a simplified marketplace flow for farmers, buyers, and node managers.

---

## 🚀 Project Vision

Farmers often face problems such as low selling prices, delayed payments, high logistics costs, lack of transparency, and wastage due to poor demand-supply coordination.

Krishi Sangam aims to solve these problems by creating a structured digital marketplace where farmers can list produce, Agro Nodes can verify quality and quantity, and buyers can purchase verified bulk produce with transparent pricing.

---

## 🌱 Core Idea

Krishi Sangam works on the **Agro Node model**.

An Agro Node can be a local collection point such as a panchayat building, village collection center, local warehouse, or verified rural hub.

### Basic Flow

1. Farmer lists produce in the app.
2. Farmer drops produce at a nearby Agro Node.
3. Node Manager verifies quality and quantity.
4. Product becomes visible to buyers after approval.
5. Buyer places a bulk/community order.
6. Farmer receives payment in two parts.
7. Order is dispatched through local logistics.

---

## 👥 User Roles

### 👨‍🌾 Farmer

Farmers can:

- Add product listings
- View mandi price and Krishi Sangam suggested price
- Enter expected price within a fair price range
- Track product approval status
- View received orders
- Track earnings and payments
- Manage farmer profile
- Use the app in multiple Indian languages

### 🛒 Buyer

Buyers can:

- Browse verified farmer listings
- View products approved by Agro Nodes
- Add products to orders
- View transparent bill split
- Track orders
- Manage buyer profile
- Change app language

### 🧑‍💼 Node Manager

Node Managers can:

- Review farmer product listings
- Approve or reject products
- Verify quality and quantity
- Manage farmer listings
- Track order dispatch
- Support transparent marketplace operations

---

## ✨ Key Features

- Farmer product listing
- Agro Node based verification system
- Buyer marketplace
- Buyer order/cart system
- Transparent bill split
- Mandi price display
- Krishi Sangam suggested price
- Farmer expected price validation
- Price fairness score
- Farmer trust score
- Farmer earnings dashboard
- Product approval status
- Multilingual support
- Firebase Authentication
- Firebase Firestore integration
- Jetpack Compose based UI

---

## 🌐 Multilingual Support

Krishi Sangam is being developed as a multilingual Android app using Android string resources.

Planned languages include:

- English
- Hindi
- Gujarati
- Marathi
- Tamil
- Telugu
- Kannada
- Malayalam
- Bengali
- Punjabi

The app uses `stringResource(R.string.key_name)` in Jetpack Compose screens so that UI text can change according to the selected language.

---

## 💰 Pricing Logic

Krishi Sangam includes a fair price suggestion system for farmers.

The app compares:

- Current mandi price
- Farmer expected price
- Krishi Sangam suggested price

The system also shows:

- Difference from mandi price
- Difference from suggested price
- Farmer profit gain percentage
- Fairness score
- Underpriced or overpriced warning

Basic pricing idea:

```text
Krishi Sangam Price = Mandi Price + Farmer Uplift + Middleman Margin Saved
