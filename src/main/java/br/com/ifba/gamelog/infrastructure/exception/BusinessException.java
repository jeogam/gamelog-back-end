package br.com.ifba.gamelog.infrastructure.exception;

/**
 * Exceção personalizada para representar violações de regras de negócio no sistema GameLog.
 * <p>
 * Esta exceção é do tipo {@link RuntimeException} (não checada), permitindo que o controle
 * transacional do Spring faça o rollback automático quando ela for lançada.
 * </p>
 * <p>
 * Ela é capturada e tratada globalmente pelo {@link GlobalExceptionHandler}.
 * </p>
 *
 * @author Seu Nome
 * @see BusinessExceptionMessage
 * @see GlobalExceptionHandler
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constrói uma nova BusinessException com a mensagem detalhada especificada.
     *
     * @param message A mensagem detalhada (que é salva para recuperação posterior pelo método {@link #getMessage()}).
     */
    public BusinessException(final String message) {
        super(message);
    }

    /**
     * Constrói uma nova BusinessException com a causa especificada.
     * Útil para envolver outras exceções (exception wrapping).
     *
     * @param cause A causa (que é salva para recuperação posterior pelo método {@link #getCause()}).
     * (Um valor null é permitido e indica que a causa é inexistente ou desconhecida.)
     */
    public BusinessException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constrói uma nova BusinessException com a mensagem detalhada e a causa especificadas.
     *
     * @param message A mensagem detalhada.
     * @param cause   A causa.
     */
    public BusinessException(final String message, final Throwable cause) {
        super(message, cause);
    }
}