package models

data class ExposedReview(
    val id: Long = 0,
    val service: Service,
    val quality: Int,
    val phone: Phone,
)

data class Phone(
    val phone: String,
    val name: String
)