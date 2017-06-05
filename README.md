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

# Future Work

- Create method to allow branch to merge back in (Tricky since it needs to have the same type as the value going into the branch)
- Extend the functor to be a full monad
