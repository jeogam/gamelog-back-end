package br.com.ifba.gamelog.infrastructure.exception;

/**
 * Enum que define as mensagens de erro de negócio do GameLog.
 * Utilizado para padronizar o retorno de exceções via GlobalExceptionHandler.
 */
public enum BusinessExceptionMessage {

    // Genéricos
    NOT_FOUND("O registro solicitado não foi encontrado na base de dados."),
    INVALID_DATA("Os dados fornecidos são inválidos."),
    ATTRIBUTE_VALUE_ALREADY_EXISTS("O valor do atributo '%s' já está em uso."),
    INVALID_CREDENTIALS("Credenciais inválidas."),
    USER_NOT_FOUND("Usuário não encontrado."),
    ID_MISMATCH("O ID informado na URL não corresponde ao ID informado no corpo da requisição."),
    CLASS_IN_USE("O registro não pode ser excluído pois está vinculado a outros dados.");

    private final String message;

    BusinessExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Método utilitário para formatar mensagens dinâmicas.
     * Exemplo: getAttributeValueAlreadyExistsMessage("Email") -> "O valor do atributo 'Email' já está em uso."
     *
     * @param attribute O nome do campo/atributo para formatar na mensagem.
     * @return A mensagem formatada.
     */
    public String getAttributeValueAlreadyExistsMessage(String attribute) {
        return String.format(message, attribute);
    }
}