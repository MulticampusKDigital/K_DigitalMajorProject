from django_request_mapping import UrlPattern

from cleaner.views import MyView

urlpatterns = UrlPattern();
urlpatterns.register(MyView);