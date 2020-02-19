package br.com.damoreira.commons.axon.mongo;

import org.axonframework.common.lock.LockFactory;
import org.axonframework.common.lock.NullLockFactory;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.messaging.annotation.HandlerDefinition;
import org.axonframework.messaging.annotation.ParameterResolverFactory;
import org.axonframework.modelling.command.AggregateNotFoundException;
import org.axonframework.modelling.command.LockingRepository;
import org.axonframework.modelling.command.inspection.AggregateModel;
import org.axonframework.modelling.command.inspection.AnnotatedAggregate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;
import java.util.function.Function;

import static java.lang.String.format;
import static org.axonframework.common.Assert.isTrue;
import static org.axonframework.common.BuilderUtils.assertNonNull;

public class AggregateMongoRepository<T, ID> extends LockingRepository<T, AnnotatedAggregate<T>> {

    private MongoRepository<T, ID> repository;

    private EventBus eventBus;

    private Function<String, ID> aggregateIdentifierConverter;

    public static <T, ID> AggregateMongoRepository.Builder<T, ID> builder(Class<T> aggregateType, Function<String, ID> aggregateIdentifierConverter) {
        assertNonNull(aggregateIdentifierConverter, "AggregateIdentifierConverter may not be null");
        return new AggregateMongoRepository.Builder<>(aggregateType, aggregateIdentifierConverter);
    }

    public AggregateMongoRepository(AggregateMongoRepository.Builder<T, ID> builder) {
        super(builder);
        this.repository = builder.repository;
        this.eventBus = builder.eventBus;
        this.aggregateIdentifierConverter = builder.aggregateIdentifierConverter;
    }

    @Override
    protected AnnotatedAggregate<T> doLoadWithLock(String aggregateIdentifier, Long expectedVersion) {
        T aggregateRoot = repository.findById(aggregateIdentifierConverter.apply(aggregateIdentifier))
            .orElseThrow(() -> new AggregateNotFoundException(aggregateIdentifier, format("Aggregate [%s] with identifier [%s] not found", getAggregateType().getSimpleName(), aggregateIdentifier)));

        AnnotatedAggregate<T> aggregate = AnnotatedAggregate.initialize(aggregateRoot, aggregateModel(), eventBus);

        Field lockVersionField = ReflectionUtils.findField(getAggregateType(), "_lockVersion", Long.class);
        lockVersionField.setAccessible(true);

        Long version = (Long) ReflectionUtils.getField(lockVersionField, aggregateRoot);
        aggregate.initSequence(version);

        return aggregate;
    }

    @Override
    protected AnnotatedAggregate<T> doCreateNewForLock(Callable<T> factoryMethod) throws Exception {
        return AnnotatedAggregate.initialize(factoryMethod, aggregateModel(), eventBus, null, true);
    }

    @Override
    protected void doSaveWithLock(AnnotatedAggregate<T> aggregate) {
        repository.save(aggregate.getAggregateRoot());
    }

    @Override
    protected void doDeleteWithLock(AnnotatedAggregate<T> aggregate) {
        Field field = ReflectionUtils.findField(getAggregateType(), "deleted", Boolean.class);
        ReflectionUtils.setField(field, aggregate.getAggregateRoot(), true);
    }

    public static class Builder<T, ID> extends LockingRepository.Builder<T> {

        private MongoRepository<T, ID> repository;

        private EventBus eventBus;

        private Function<String, ID> aggregateIdentifierConverter;

        protected Builder(Class<T> aggregateType, Function<String, ID> aggregateIdentifierConverter) {
            super(aggregateType);
            super.lockFactory(NullLockFactory.INSTANCE);
            this.aggregateIdentifierConverter = aggregateIdentifierConverter;

            Field deletedField = ReflectionUtils.findField(aggregateType, "deleted", Boolean.class);
            assertNonNull(deletedField, "AggregateType must have a Boolean deleted field.");

            Field lockVersionField = ReflectionUtils.findField(aggregateType, "_lockVersion", Long.class);
            assertNonNull(lockVersionField, "AggregateType must have a Long _lockVersion field.");
            isTrue(lockVersionField.isAnnotationPresent(Version.class), () -> "AggregateType field _lockVersion must be annotated with @Version.");
        }

        @Override
        public AggregateMongoRepository.Builder<T, ID> parameterResolverFactory(ParameterResolverFactory parameterResolverFactory) {
            super.parameterResolverFactory(parameterResolverFactory);
            return this;
        }

        @Override
        public AggregateMongoRepository.Builder<T, ID> handlerDefinition(HandlerDefinition handlerDefinition) {
            super.handlerDefinition(handlerDefinition);
            return this;
        }

        @Override
        public AggregateMongoRepository.Builder<T, ID> aggregateModel(AggregateModel<T> aggregateModel) {
            super.aggregateModel(aggregateModel);
            return this;
        }

        @Override
        public AggregateMongoRepository.Builder<T, ID> lockFactory(LockFactory lockFactory) {
            super.lockFactory(lockFactory);
            return this;
        }

        public AggregateMongoRepository.Builder<T, ID> repository(MongoRepository<T, ID> repository) {
            assertNonNull(repository, "MongoRepository may not be null");
            this.repository = repository;
            return this;
        }

        public AggregateMongoRepository.Builder<T, ID> eventBus(EventBus eventBus) {
            assertNonNull(eventBus, "EventBus may not be null");
            this.eventBus = eventBus;
            return this;
        }

        public AggregateMongoRepository<T, ID> build() {
            return new AggregateMongoRepository<>(this);
        }

        @Override
        protected void validate() {
            super.validate();
        }

    }

}
