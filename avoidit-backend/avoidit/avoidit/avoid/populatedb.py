from avoid.models import AvoidItUser, Rule, ContactList, Entry

user = AvoidItUser(first_name="fn",
                   last_name="ln",
                   phone_number="1234567890",
                   password="test",
                   email="test@test.com",)

user.save()

user = AvoidItUser.objects.get(phone_number="1234567890")
user.first_name = "bob"

user.save()