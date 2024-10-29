data class Question(
    val id: Any,
    val question: String,
    val relatedQuestions: List<Question> = emptyList(),
    val expectations: List<String> = emptyList()
)

class QuestionScope {
    lateinit var id: Any
    lateinit var text: String
    var expectations = emptyList<String>()
    private var questions = mutableListOf<QuestionScope>()

    fun question(id: Any, tree: QuestionScope.() -> Unit) {
        val questionScope = QuestionScope()
        questionScope.id = id

        tree(questionScope)
        questions.add(questionScope)
    }

    fun asQuestion(): Question = Question(
        id = id,
        question = text,
        relatedQuestions = questions.map { it.asQuestion() },
        expectations = expectations
    )
}

fun question(id: Any, tree: QuestionScope.() -> Unit): Question {
    val scope = QuestionScope()
    scope.id = id
    tree(scope)
    return scope.asQuestion()
}