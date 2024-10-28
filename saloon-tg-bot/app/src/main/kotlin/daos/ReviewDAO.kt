package daos

import models.ExposedReview
import models.Service
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class ReviewDAO(private val database: Database? = null) : DAO<ExposedReview> {
    object Reviews : LongIdTable() {
        val service = enumeration<Service>("service")
        val quality = integer("quality")
        val phone = varchar("phone", 12)
        val reviewer = varchar("reviewer", 256)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Reviews)
        }
    }

    override fun create(obj: ExposedReview) = transaction(database) {
        Reviews.insert {
            it[service] = obj.service
            it[quality] = obj.quality
            it[phone] = obj.phone
            it[reviewer] = obj.reviewer
        }[Reviews.id].value
    }

    override fun read(id: Long) = transaction(database) {
        Reviews.selectAll()
            .where { Reviews.id eq id }
            .map {
                ExposedReview(
                    id = it[Reviews.id].value,
                    service = it[Reviews.service],
                    quality = it[Reviews.quality],
                    phone = it[Reviews.phone],
                    reviewer = it[Reviews.reviewer]
                )
            }.singleOrNull()
    }

    override fun readAll() = transaction(database) {
        Reviews.selectAll()
            .map {
                ExposedReview(
                    id = it[Reviews.id].value,
                    service = it[Reviews.service],
                    quality = it[Reviews.quality],
                    phone = it[Reviews.phone],
                    reviewer = it[Reviews.reviewer]
                )
            }.toList()
    }

    override fun update(id: Long, obj: ExposedReview) = transaction(database) {
        Reviews.update({ Reviews.id eq id }) {
            it[service] = obj.service
            it[quality] = obj.quality
            it[phone] = obj.phone
            it[reviewer] = obj.reviewer
        } != 0
    }

    override fun delete(id: Long) = transaction(database) {
        Reviews.deleteWhere { Reviews.id eq id } != 0
    }
}