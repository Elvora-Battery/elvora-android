package com.unsoed.elvora.dummy

import com.unsoed.elvora.data.Rental

val rentalDataList = listOf(
    Rental(
        id = 1,
        price = "Rp 250.000",
        type = "Gesits 72V 20Ah Battery",
        filter = "Gesits",
        description = "The mandatory rental period is 3 years",
        capacity = 20,
        priceInt = 250.000
    ),
    Rental(
        id = 2,
        price = "Rp 400.000",
        type = "Gesits 72V 40Ah Battery",
        description = "The mandatory rental period is 3 years 4 month",
        filter = "Gesits",
        capacity = 40,
        priceInt = 400.000
    ),
    Rental(
        id = 2,
        price = "Rp 400.000",
        type = "Alva CERVO 72V 40Ah Battery",
        description = "The mandatory rental period is 3 years 4 month",
        filter = "Alva",
        capacity = 40,
        priceInt = 400.000
    ),
    Rental(
        id = 2,
        price = "Rp 400.000",
        type = "Selis E-Max 72V 40Ah Battery",
        filter = "Selis",
        description = "The mandatory rental period is 3 years 4 month",
        capacity = 40,
        priceInt = 400.000
    ),
)