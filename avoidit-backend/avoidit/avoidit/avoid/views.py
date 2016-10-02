from django.shortcuts import render
from django.http import HttpResponse
from .models import AvoidItUser

def avoid(request):
    return HttpResponse("Server is up!")
