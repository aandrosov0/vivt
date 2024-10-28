package daos

interface DAO<T> {
    fun create(obj: T): Long
    fun read(id: Long): T?
    fun readAll(): List<T>
    fun update(id: Long, obj: T): Boolean
    fun delete(id: Long): Boolean
}