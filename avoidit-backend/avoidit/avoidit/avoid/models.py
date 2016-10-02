from __future__ import unicode_literals

from django.db import models

# Create your models here.
class AvoidItUser(models.Model):
    first_name = models.CharField(max_length=20)
    last_name = models.CharField(max_length=20)
    phone_number = models.CharField(max_length=13, primary_key=True)
    password = models.CharField(max_length=255)
    email = models.EmailField()
    verified  = models.BooleanField(default=False)

class Rule(models.Model):
    phone_number = models.ForeignKey(AvoidItUser, on_delete=models.CASCADE)
    rid = models.IntegerField(primary_key=True)
    rule_name = models.CharField(max_length=30)
    passes = models.IntegerField()

class ContactList(models.Model):
    rid = models.ForeignKey(Rule, on_delete=models.CASCADE)
    contact = models.CharField(max_length=13)

class Entry(models.Model):
    CATEGORY = 'CA'
    LOCATION = 'LO'
    PRICE = 'PR'

    CATEGORY_CHOICES = (
        (LOCATION, 'Location'),
        (CATEGORY, 'Category'),
        (PRICE, 'Price'),
    )
    eid = models.IntegerField(primary_key=True)
    category = models.CharField(max_length=2,choices=CATEGORY_CHOICES)
    category_specs = models.CharField(max_length=2000)
    alert_type = models.CharField(max_length=255)
    rid = models.ForeignKey(Rule, on_delete=models.CASCADE)
