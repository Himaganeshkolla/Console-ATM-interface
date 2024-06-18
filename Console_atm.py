# Store user data and balances in dictionaries
users = {"user1": "1234", "user2": "5678"}
balances = {"user1": 1000, "user2": 1500}
transactions = {"user1": [], "user2": []}

def authenticate(user_id, pin):
    return users.get(user_id) == pin

def show_menu():
    print("\n1. Transaction History")
    print("2. Withdraw")
    print("3. Deposit")
    print("4. Transfer")
    print("5. Quit")

def transaction_history(user_id):
    if not transactions[user_id]:
        print("No transactions yet.")
    else:
        for transaction in transactions[user_id]:
            print(transaction)

def withdraw(user_id, amount):
    if amount > balances[user_id]:
        print("Insufficient funds")
    else:
        balances[user_id] -= amount
        transactions[user_id].append(f"Withdrew: ${amount}")
        print(f"Withdrawal successful. New balance: ${balances[user_id]}")

def deposit(user_id, amount):
    balances[user_id] += amount
    transactions[user_id].append(f"Deposited: ${amount}")
    print(f"Deposit successful. New balance: ${balances[user_id]}")

def transfer(user_id, target_user_id, amount):
    if amount > balances[user_id]:
        print("Insufficient funds")
    else:
        balances[user_id] -= amount
        balances[target_user_id] += amount
        transactions[user_id].append(f"Transferred: ${amount} to {target_user_id}")
        transactions[target_user_id].append(f"Received: ${amount} from {user_id}")
        print(f"Transfer successful. New balance: ${balances[user_id]}")

def main():
    user_id = input("Enter user ID: ")
    pin = input("Enter PIN: ")

    if not authenticate(user_id, pin):
        print("Authentication failed")
        return

    while True:
        show_menu()
        choice = input("Choose an option: ")
        if choice == "1":
            transaction_history(user_id)
        elif choice == "2":
            amount = float(input("Enter amount to withdraw: "))
            withdraw(user_id, amount)
        elif choice == "3":
            amount = float(input("Enter amount to deposit: "))
            deposit(user_id, amount)
        elif choice == "4":
            target_user_id = input("Enter target user ID: ")
            if target_user_id not in balances:
                print("Target account not found")
                continue
            amount = float(input("Enter amount to transfer: "))
            transfer(user_id, target_user_id, amount)
        elif choice == "5":
            print("Goodbye!")
            break
        else:
            print("Invalid choice")

if __name__ == "__main__":
    main()
