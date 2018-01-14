import sys
import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
from firebase_admin import auth


#The function print all the users details exept the password and the img
def showAllUsers():
    ref = db.reference('users')
    print "All the App users"
    dict = ref.get()
    if dict != None:			
		for user in dict.values():
			for key, val in user.items():
				if key != '_img' and key != '_password':
					print key + ": " + val
			print "\n"
    else:
		print "No users in the database"

#The function delete user / update user info by userName
def deleteUser():
	ref = db.reference('users')
	dict = ref.get()
	if dict != None:
		# show the users in the system by userName
		print "The users in the system: "
		for user in dict.values():
			for key, val in user.items():
				if key == '_userName':
					print "User: " + val
		# find the user to delete by input userName
		toDelete = raw_input("enter the name that you want to delete >> ")
		for user in dict.values():
			for key, val in user.items():
				if key == '_userName':
					if val == toDelete:
						ref.child(val).delete()
						print "successfully deleted " + val
						return

# The function add users to the DB
def addUser():
	userName = (raw_input("enter username >> "))
	name = (raw_input("enter the user first name >> "))
	lastName = (raw_input("enter user last name >> "))
	city = (raw_input("enter user city >> "))
	address = (raw_input("enter user address >> "))
	email = (raw_input("enter user email address >> "))
	password = (raw_input("enter the user password >> "))
	ref = db.reference('users').child(userName)
	ref.set({
		'_userName' : userName,
		'_name' : name,
		'_lastName' : lastName,
		'_city' : city,
		'_address' : address,
		'_email' : email,
		'_password' : password,
		})
	print "Successfully created user"

def main():
	#CLI menu
	print "Welcome,\n"
	print "Please choose the action you want start: "
	print "1. show all users"
	print "2. delete user by name"
	print "3. add new user"
	print "0. Quit"
	while True:
		try:
			choice = int(raw_input(" >> "))
			if choice != 0 and choice != 1 and choice != 2 and choice != 3:
				raise ValueError
			else:
				exec_menu(choice)
		except ValueError, ex:
			print "Error, enter again", ex
			continue

def exec_menu(choice):
	if choice == 0:
		print "Thanks, shiftApp Team"
		sys.exit()
	elif choice == 1:
		showAllUsers()
	elif choice == 2:
		deleteUser()
	elif choice == 3:
		addUser()

#my Application fireBase connection
cred = credentials.Certificate("myapplication2-b3625-firebase-adminsdk-c1obw-cd3b0a813e.json")
firebase_admin.initialize_app(cred, {'databaseURL': 'https://myapplication2-b3625.firebaseio.com'})

if __name__ == '__main__':
	main()
