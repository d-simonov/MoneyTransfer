package de.dsimonov.moneytransfer;

import de.dsimonov.moneytransfer.model.Account;
import de.dsimonov.moneytransfer.repository.impl.InMemoryAccountRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class TestAccountConcurrency {

    public static final int THREADS = 100;

    private InMemoryAccountRepository accountRepository = new InMemoryAccountRepository();

    @Test
    public void testAccountCreation() throws ExecutionException, InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);
        ExecutorService service = Executors.newFixedThreadPool(THREADS);
        Collection<Future<Account>> futures = new ArrayList<>(THREADS);

        for (int i = 0; i < THREADS; ++i) {
            futures.add(
                    service.submit(
                            () -> {
                                Account account = new Account("ABC", BigDecimal.ONE);
                                return accountRepository.create(account);
                            }
                    )
            );
        }

        //starting all tasks together
        latch.countDown();

        Set<Long> results = new HashSet<>();

        for (Future<Account> future : futures) {
            results.add(future.get().getId());
        }

        assertThat(hasAllNumbersInRange(THREADS, results)).isEqualTo(true);
    }

    public static boolean hasAllNumbersInRange(int n, Set<Long> set) {
        boolean result = true;

        for (long i = 1; i <= n; i++) {
            if (!set.contains(i)) {
                result = false;
                break;
            }
        }

        return result;
    }
}
