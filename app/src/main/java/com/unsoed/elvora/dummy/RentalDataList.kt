package com.unsoed.elvora.dummy

import com.unsoed.elvora.data.Activity
import com.unsoed.elvora.data.Rental

val rentalDataList = listOf(
    Rental(
        id = 1,
        price = "Rp 250.000",
        type = "72V 20Ah Battery",
        description = "The mandatory rental period is 3 years",
        capacity = 20,
        priceInt = 250.000
    ),
    Rental(
        id = 2,
        price = "Rp 400.000",
        type = "72V 40Ah Battery",
        description = "The mandatory rental period is 3 years 4 month",
        capacity = 40,
        priceInt = 400.000
    ),
)

val activityDataList = listOf(
    Activity(
        id = "EV40001",
        battery = "72V 20Ah",
        date = "28 Juni 2023, 14:08 WIB",
        status = "Cancelled"
    ),
    Activity(
        id = "EV40004",
        battery = "72V 40Ah",
        date = "28 Juny 2023, 14:08 WIB",
        status = "Waiting Payment"
    ),
    Activity(
        id = "EV20004",
        battery = "72V 20Ah",
        date = "28 January 2023, 14:08 WIB",
        status = "Delivery"
    ),
    Activity(
        id = "EV20003",
        battery = "72V 40Ah",
        date = "28 Juny 2023, 14:08 WIB",
        status = "Finish"
    )
)