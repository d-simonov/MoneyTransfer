package de.dsimonov.moneytransfer;

import de.dsimonov.moneytransfer.model.Account;
import de.dsimonov.moneytransfer.model.Transfer;
import de.dsimonov.moneytransfer.service.AccountService;
import de.dsimonov.moneytransfer.service.TransferService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestTransferConcurrency {

    //must be an even number
    //the test by design is expecting the same number of transfers from and to account
    public static final int THREADS = 100;

    @InjectMocks
    private TransferService transferService;

    @Mock
    private AccountService accountService;

    @Test
    public void testMultipleTransfers() throws ExecutionException, InterruptedException {
        MockitoAnnotations.initMocks(this);
        BigDecimal initialBalance = new BigDecimal(50000);
        Account first = new Account("ABC", initialBalance);
        Account second = new Account("DEF", initialBalance);

        when(accountService.getAccount(eq(1L))).thenReturn(first);
        when(accountService.getAccount(eq(2L))).thenReturn(second);

        CountDownLatch latch = new CountDownLatch(1);
        ExecutorService service = Executors.newFixedThreadPool(THREADS);
        Collection<Future<Boolean>> futures = new ArrayList<>(THREADS);
        BigDecimal amount = new BigDecimal(10);

        for (int i = 0; i < THREADS; ++i) {
            int finalI = i;
            futures.add(
                    service.submit(
                            () -> {
                                long sourceAcc = finalI % 2 == 0 ? 1L : 2L;
                                long targetAcc = finalI % 2 == 0 ? 2L : 1L;
                                return transferService.execute(new Transfer(sourceAcc, targetAcc, amount));
                            }
                    )
            );
        }

        //starting all tasks together
        latch.countDown();

        Set<Boolean> results = new HashSet<>();

        for (Future<Boolean> future : futures) {
            results.add(future.get());
        }

        for (Boolean result : results) {
            assertThat(result).isEqualTo(true);
        }

        assertThat(first.getBalance()).isEqualTo(initialBalance);
        assertThat(second.getBalance()).isEqualTo(initialBalance);
    }
}
