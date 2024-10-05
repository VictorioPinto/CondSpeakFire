data class SelecionaCondominioModel(
    val nome: String?,
    val CEP: String?,
    val codigo: String,
    val tipo: String
)

private val condominiolist = mutableListOf<SelecionaCondominioModel>()