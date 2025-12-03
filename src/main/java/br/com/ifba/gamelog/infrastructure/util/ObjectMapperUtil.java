package br.com.ifba.gamelog.infrastructure.util;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.record.RecordModule;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * Utilitário para mapeamento entre objetos usando {@link ModelMapper}.
 * Adaptado para br.com.ifba.gamelog
 */
@Component
public class ObjectMapperUtil {

    private static final ModelMapper MODEL_MAPPER;

    static {
        MODEL_MAPPER = new ModelMapper();

        MODEL_MAPPER.getConfiguration()
                .setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

        // Importante: Isso exige a dependência modelmapper-module-record no pom.xml
        MODEL_MAPPER.registerModule(new RecordModule());
    }

    /**
     * Mapeia um objeto de entrada para uma instância da classe especificada.
     */
    public <Input, Output> Output map(final Input object, final Class<Output> clazz) {
        // Reconfigura para garantir consistência em cada chamada
        MODEL_MAPPER.getConfiguration()
                .setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

        return MODEL_MAPPER.map(object, clazz);
    }

    /**
     * Copia dados de um objeto fonte para um objeto alvo (Cópia manual via Reflexão).
     */
    public <Source, Target> Target map(final Source source, Target target) {
        try {
            for (Field sourceField : source.getClass().getDeclaredFields()) {
                boolean fieldExists = Arrays.stream(target.getClass().getDeclaredFields())
                        .anyMatch(f -> f.getName().equals(sourceField.getName()));

                if (!fieldExists)
                    continue;

                Field targetField = target.getClass().getDeclaredField(sourceField.getName());
                sourceField.setAccessible(true);
                targetField.setAccessible(true);

                if (isRecord(sourceField.getType())) {
                    Object sourceAggregateObject = sourceField.get(source);
                    Object targetAggregateObject = targetField.getType().getDeclaredConstructor().newInstance();
                    targetField.set(target, map(sourceAggregateObject, targetAggregateObject));
                    continue;
                }

                Object value = sourceField.get(source);
                targetField.set(target, value);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return target;
    }

    /**
     * Mapeia um objeto para um Java Record manualmente.
     * Útil quando o ModelMapper padrão não consegue resolver construtores complexos.
     */
    public <T> T mapToRecord(Object source, Class<T> recordClass) {
        try {
            var components = recordClass.getRecordComponents();

            Object[] args = Arrays.stream(components)
                    .map(c -> {
                        try {
                            // Lógica inteligente: Se o campo do DTO termina em "Id" (ex: usuarioId),
                            // ele tenta pegar o ID do objeto relacionado na entidade.
                            if (c.getName().endsWith("Id")) {
                                String aggregateName = c.getName().replace("Id", "");
                                Field f = getFieldFromHierarchy(source.getClass(), aggregateName);
                                if (f != null) {
                                    f.setAccessible(true);
                                    Object aggregate = f.get(source);
                                    if (aggregate != null) {
                                        Method getId = aggregate.getClass().getMethod("getId");
                                        return getId.invoke(aggregate);
                                    }
                                }
                                return null;
                            }

                            // Campo normal
                            Field f = getFieldFromHierarchy(source.getClass(), c.getName());
                            if (f == null) return null;
                            f.setAccessible(true);
                            return f.get(source);

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toArray();

            return recordClass.getDeclaredConstructor(
                            Arrays.stream(components).map(c -> c.getType()).toArray(Class[]::new))
                    .newInstance(args);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Field getFieldFromHierarchy(Class<?> clazz, String fieldName) {
        while (clazz != null) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }

    private boolean isRecord(Class<?> clazz) {
        return clazz.isRecord();
    }

    public <Input, Output> Function<Input, Output> mapFn(final Class<Output> clazz) {
        return object -> MODEL_MAPPER.map(object, clazz);
    }

    public <Input, Output> List<Output> mapAll(final Collection<Input> objectList, Class<Output> clazz) {
        MODEL_MAPPER.getConfiguration()
                .setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

        return objectList.stream()
                .map(obj -> MODEL_MAPPER.map(obj, clazz))
                .toList();
    }

    public <Input, Output> Function<List<Input>, List<Output>> mapAllFn(final Class<Output> clazz) {
        return objectList -> objectList.stream()
                .map(object -> MODEL_MAPPER.map(object, clazz))
                .toList();
    }
}