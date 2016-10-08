package dagger.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Provider;

public final class SetFactory<T> implements Factory<Set<T>> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final String ARGUMENTS_MUST_BE_NON_NULL = "SetFactory.create() requires its arguments to be non-null";
    private static final Factory<Set<Object>> EMPTY_FACTORY;
    private final List<Provider<Set<T>>> contributingProviders;

    /* renamed from: dagger.internal.SetFactory.1 */
    static final class C11551 implements Factory<Set<Object>> {
        C11551() {
        }

        public Set<Object> get() {
            return Collections.emptySet();
        }
    }

    static {
        $assertionsDisabled = !SetFactory.class.desiredAssertionStatus() ? true : $assertionsDisabled;
        EMPTY_FACTORY = new C11551();
    }

    private SetFactory(List<Provider<Set<T>>> list) {
        this.contributingProviders = list;
    }

    public static <T> Factory<Set<T>> create() {
        return EMPTY_FACTORY;
    }

    public static <T> Factory<Set<T>> create(Factory<Set<T>> factory) {
        if ($assertionsDisabled || factory != null) {
            return factory;
        }
        throw new AssertionError(ARGUMENTS_MUST_BE_NON_NULL);
    }

    public static <T> Factory<Set<T>> create(Provider<Set<T>>... providerArr) {
        if ($assertionsDisabled || providerArr != null) {
            List asList = Arrays.asList(providerArr);
            if (!$assertionsDisabled && asList.contains(null)) {
                throw new AssertionError("Codegen error?  Null within provider list.");
            } else if ($assertionsDisabled || !hasDuplicates(asList)) {
                return new SetFactory(asList);
            } else {
                throw new AssertionError("Codegen error?  Duplicates in the provider list");
            }
        }
        throw new AssertionError(ARGUMENTS_MUST_BE_NON_NULL);
    }

    private static boolean hasDuplicates(List<? extends Object> list) {
        return list.size() != new HashSet(list).size() ? true : $assertionsDisabled;
    }

    public Set<T> get() {
        int i;
        List arrayList = new ArrayList(this.contributingProviders.size());
        int size = this.contributingProviders.size();
        int i2 = 0;
        for (i = 0; i < size; i++) {
            Provider provider = (Provider) this.contributingProviders.get(i);
            Set set = (Set) provider.get();
            if (set == null) {
                String valueOf = String.valueOf(provider);
                throw new NullPointerException(new StringBuilder(String.valueOf(valueOf).length() + 14).append(valueOf).append(" returned null").toString());
            }
            arrayList.add(set);
            i2 = set.size() + i2;
        }
        Set newLinkedHashSetWithExpectedSize = Collections.newLinkedHashSetWithExpectedSize(i2);
        i = arrayList.size();
        for (int i3 = 0; i3 < i; i3++) {
            for (Object next : (Set) arrayList.get(i3)) {
                if (next == null) {
                    throw new NullPointerException("a null element was provided");
                }
                newLinkedHashSetWithExpectedSize.add(next);
            }
        }
        return Collections.unmodifiableSet(newLinkedHashSetWithExpectedSize);
    }
}
