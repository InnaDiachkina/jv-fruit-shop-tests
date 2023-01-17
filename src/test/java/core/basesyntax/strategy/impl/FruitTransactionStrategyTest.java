package core.basesyntax.strategy.impl;

import core.basesyntax.dao.FruitDao;
import core.basesyntax.dao.FruitDaoImpl;
import core.basesyntax.db.FruitStorage;
import core.basesyntax.model.FruitTransaction;
import core.basesyntax.strategy.FruitTransactionHandler;
import core.basesyntax.strategy.FruitTransactionStrategy;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FruitTransactionStrategyTest {
    private static FruitTransactionStrategy fruitTransactionStrategy;
    private static FruitTransaction fruitTransaction;
    private static FruitDao fruitDao;
    private FruitTransactionHandler actual;

    @BeforeClass
    public static void beforeClass() {
        fruitTransactionStrategy = new FruitTransactionStrategyImpl();
        fruitDao = new FruitDaoImpl();
    }

    @Before
    public void setUp() {
        FruitStorage.storageFruits.put("banana", 100);
    }

    @Test
    public void chooseBalanceHandler_ok() {
        fruitTransaction = new FruitTransaction(FruitTransaction.Operation.BALANCE,
                "banana", 29);
        actual = fruitTransactionStrategy.getTransaction(fruitTransaction.getOperation());
        FruitTransactionHandler expected = new BalanceHandler();
        Assert.assertEquals(expected.getClass(), actual.getClass());
        actual.handleTransaction(fruitTransaction);
        Assert.assertEquals("Expected quantity 29 for banana in FruitStorage, but was "
                + fruitDao.getQuantityByName("banana"), 29,
                fruitDao.getQuantityByName("banana"));
    }

    @Test
    public void choosePurchaseHandler_ok() {
        fruitTransaction = new FruitTransaction(FruitTransaction.Operation.PURCHASE,
                "banana", 29);
        actual = fruitTransactionStrategy.getTransaction(fruitTransaction.getOperation());
        FruitTransactionHandler expected = new PurchaseHandler();
        Assert.assertEquals(expected.getClass(), actual.getClass());
        actual.handleTransaction(fruitTransaction);
        Assert.assertEquals("Expected quantity 71 for banana in FruitStorage, but was "
                + fruitDao.getQuantityByName("banana"), 71,
                fruitDao.getQuantityByName("banana"));
    }

    @Test
    public void chooseSupplyHandler_ok() {
        fruitTransaction = new FruitTransaction(FruitTransaction.Operation.SUPPLY,
                "banana", 29);
        actual = fruitTransactionStrategy.getTransaction(fruitTransaction.getOperation());
        FruitTransactionHandler expected = new SupplyHandler();
        Assert.assertEquals(expected.getClass(), actual.getClass());
        actual.handleTransaction(fruitTransaction);
        Assert.assertEquals("Expected quantity 129 for banana in FruitStorage, but was "
                + fruitDao.getQuantityByName("banana"), 129,
                fruitDao.getQuantityByName("banana"));
    }

    @Test
    public void chooseReturnHandler_ok() {
        fruitTransaction = new FruitTransaction(FruitTransaction.Operation.RETURN,
                "banana", 29);
        actual = fruitTransactionStrategy.getTransaction(fruitTransaction.getOperation());
        FruitTransactionHandler expected = new ReturnHandler();
        Assert.assertEquals(expected.getClass(), actual.getClass());
        actual.handleTransaction(fruitTransaction);
        Assert.assertEquals("Expected quantity 129 for banana in FruitStorage, but was "
                + fruitDao.getQuantityByName("banana"), 129,
                fruitDao.getQuantityByName("banana"));
    }

    @Test(expected = NullPointerException.class)
    public void nullOperator_notOk() {
        fruitTransaction = new FruitTransaction(null,
                "banana", 29);
        actual = fruitTransactionStrategy.getTransaction(fruitTransaction.getOperation());
        actual.handleTransaction(fruitTransaction);
    }

    @After
    public void tearDown() {
        FruitStorage.storageFruits.clear();
    }
}
