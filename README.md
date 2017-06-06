# Stated Functional Programming (statedfp)

This library exposes a functor that, on top of normal transformations, allows for the containment of 'state'. This state is exposed in such a way that it can be used in any call to 'map', effectively acting as an ecapsulated stated for the transforamtion. In practice, this can be any object; but, in theory, this object should be an effectively immutable, data structure.

In addition to the allowance of a 'state', this functor also exposes a means for creating 'branches' in the flow of operation in a transform. Branches are created by calling the 'branch' method and supplieing a predicate that consumes either the current value, or the value and state. If the predicate returns false, the branch is ignored and normal operation resumes. If the predicate returns true, the branch is executed, and the rest of the transform is ignored.

```
Consumer<StateObject> operation = ProcedureContainer
            .initializeContainer(StateObject::getInitialValue)
            .map((v) -> createNewValueFromInitial(v))
            .map((s, v) -> createValueFromStateAndOldValue(s, v))
            
            .branch(UtiliyClass::doesValueNotMeetCrieteria)
            .map((v) -> createNewValueFromDud(v))
            .endBranch((v) -> consumeVlaue(v))
            
            .map((v) -> runOneLastTransformOnGoodValue(v))
            .terminate((v) -> consumeValue(v);
            
operation.accept(operationState);
```

# Branching
In line with the branching functionality is a `test` branching method. Unlike the `branch` method that eventually ends in termination, the `test` method allows alternative paths of data transformation. After calling test() with a predicate, two methods are expose: `then` and `thenOrElse`. Then takes a single BiFunction that performs a transform on the data value, with the restriction that the same type must be returned. This allows for merging back into the fow of the overall transform.
```
Consumer<StateObject> operation = ProcedureContainer
            .initializeContainer(StateObject::getInitialValue)
            .map((v) -> createNewValueFromInitial(v))
            .test((v) -> !isValueUpToPar(v))
            .then((v) -> makeValueThatIsUpToPar(v))
            .terminate((v) -> consumerValue(v));
```
Here, the `makeValueUpToPar` method will only be run if the `test` passes. As mentioned, `makeValueUpToPar` must be type invariant (returns the same type as it takes in). To get around this restriction, the `thenOrElse` method can be used. 
```
Consumer<StateObject> operation = ProcedureContainer
            .initializeContainer(StateObject::getInitialValue)
            .map((v) -> createNewValueFromInitial(v))
            .test((v) -> containsProperty(v))
            .thenOrElse(
                (s, v) -> transformUsingProperty(v),
                (s, v) -> transformWithoutProperty(v))
            .terminate((v) -> consumerValue(v));
```


# Profunctor

In the case where a complex transform must be performed on an intermediate value, it is possible to use generate a function from a ProcedureContainer. This can be done by calling the get


