package ch.grignola.service.balance;

import java.util.List;

public class TokenBalances {
    private final List<TokenBalance> balances;
    private final List<TokenBalanceError> errors;

    public TokenBalances(List<TokenBalance> balances, List<TokenBalanceError> errors) {
        this.balances = balances;
        this.errors = errors;
    }

    public List<TokenBalance> getBalances() {
        return balances;
    }

    public List<TokenBalanceError> getErrors() {
        return errors;
    }

    static class TokenBalanceError {
        private final String address;
        private final String error;

        public TokenBalanceError(String address, String error) {
            this.address = address;
            this.error = error;
        }

        public String getAddress() {
            return address;
        }

        public String getError() {
            return error;
        }
    }
}
