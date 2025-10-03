import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import AboutView from "../views/AboutView.vue";
import ErrorNotFoundView from "../views/ErrorNotFoundView.vue";
import LoginForm from "@/components/LoginForm.vue";

import auth from "@/common/auth";
import { getStore } from "@/common/store";
import SignupForm from "@/components/SignupForm";
import EventsView from "@/views/EventsView";
import EventDetailsView from "@/views/EventDetailsView.vue";
import UsersView from "@/views/UsersView";
import UserDetailsView from "@/views/UserDetailsView.vue";
import EventCategoriesView from "@/views/EventCategoriesView";
import ProfileView from "@/views/ProfileDetailView.vue";
import ProfileHostedEvents from "@/components/profile/ProfileHostedEvents.vue";
import ProfileSavedEvents from "@/components/profile/ProfileSavedEvents.vue";
import ProfileSubscribedEvents from "@/components/profile/ProfileSubscribedEvents.vue";
import ProfileSubscribedCategories from "@/components/profile/ProfileSubscribedCategories.vue";
import ProfileEditView from "@/views/ProfileEditView.vue";
import EventCreate from "@/components/events/EventCreate.vue";
import EventEditView from "@/views/EventEditView";
import UserFollowers from "@/components/users/UserFollowers";
import UserFollowing from "@/components/users/UserFollowing";
import AdminView from "@/views/AdminView";
import AdminEvents from "@/components/admin/AdminEvents";
import UserRepository from "@/repositories/UserRepository";
import AdminCategories from "@/components/admin/AdminCategories";
import AdminUsers from "@/components/admin/AdminUsers";
import EventCategoryCreate from "@/components/categories/EventCategoryCreate";
import EventCategoryEditView from "@/views/EventCategoryEditView";
import UserEditView from "@/views/UserEditView";
import SuspendedUserView from "@/views/SuspendedUserView";

const routes = [
  {
    path: "/login",
    name: "Login",
    component: LoginForm,
    meta: { public: true, isLoginPage: true },
  },
  {
    path: "/signup",
    name: "Signup",
    component: SignupForm,
    meta: { public: true, isSignupPage: true },
  },
  {
    path: "/suspended",
    name: "Suspended",
    component: SuspendedUserView,
    meta: { public: true, isSuspendedPage: true },
  },
  {
    path: "/",
    name: "Home",
    component: HomeView,
    meta: { public: true },
  },
  {
    path: "/events",
    name: "Events",
    component: EventsView,
    meta: { public: true },
  },
  {
    path: "/events/:id",
    name: "Event Details",
    component: EventDetailsView,
    meta: { public: true },
  },
  {
    path: "/events/create",
    name: "Create Event",
    component: EventCreate,
    meta: { public: false },
  },
  {
    path: "/events/:id/edit",
    name: "Edit Event",
    component: EventEditView,
    meta: { public: false },
    beforeEnter: async (to, from, next) => {
      // Verify if the user trying to access is the event host
      const eventId = to.params.id;

      // Verificar si el evento pertenece al usuario actual
      const events = await UserRepository.findUserEvents(
        getStore().state.user.id,
        null,
        null
      );

      const isEventOwner = !!events.find(
        (event) => event.id.toString() === eventId
      );

      if (isEventOwner || getStore().state.user.authority === "ADMIN") {
        // El evento pertenece al usuario o el usuario es administrador, permite el acceso a la ruta
        next();
      } else {
        // El evento no pertenece al usuario, mostrar un mensaje de error
        alert("Acceso prohibido para el usuario actual");
        next("/profile");
      }
    },
  },
  {
    path: "/event-categories",
    name: "Event Categories",
    component: EventCategoriesView,
    meta: { public: true },
  },
  {
    path: "/event-categories/:id", // return the events of the category
    name: "Event Category Detail",
    component: EventsView,
    meta: { public: true },
  },
  {
    path: "/event-categories/create",
    name: "Create Event Category",
    component: EventCategoryCreate,
    meta: { public: false, authority: "ADMIN" },
  },
  {
    path: "/event-categories/:id/edit",
    name: "Edit Event Category",
    component: EventCategoryEditView,
    meta: { public: false, authority: "ADMIN" },
  },
  {
    path: "/members",
    name: "Members",
    component: UsersView,
    meta: { public: true },
  },
  {
    path: "/members/:id",
    name: "Member Detail",
    component: UserDetailsView,
    meta: { public: true },
  },
  {
    path: "/members/:id/edit",
    name: "Edit Member",
    component: UserEditView,
    meta: { public: false, authority: "ADMIN" },
  },
  {
    path: "/about",
    name: "About",
    component: AboutView,
    meta: { public: true },
  },
  {
    path: "/profile",
    name: "Profile",
    component: ProfileView,
    children: [
      { path: "hosted-events", component: ProfileHostedEvents },
      { path: "saved-events", component: ProfileSavedEvents },
      { path: "subscribed-events", component: ProfileSubscribedEvents },
      { path: "subscribed-categories", component: ProfileSubscribedCategories },
      { path: "followers", component: UserFollowers },
      { path: "following", component: UserFollowing },
    ],
    meta: { public: false },
  },
  {
    path: "/profile/edit",
    name: "Edit Profile",
    component: ProfileEditView,
    meta: { public: false },
  },
  {
    path: "/admin",
    name: "Administration",
    component: AdminView,
    children: [
      { path: "events", component: AdminEvents },
      { path: "categories", component: AdminCategories },
      { path: "members", component: AdminUsers },
    ],
    meta: { public: false, authority: "ADMIN" },
  },
  {
    path: "/:catchAll(.*)*",
    component: ErrorNotFoundView,
    meta: { public: true },
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

router.beforeEach((to, from, next) => {
  // Lo primero que hacemos antes de cargar ninguna ruta es comprobar si
  // el usuario está autenticado (revisando el token)
  auth.isAuthenticationChecked.finally(() => {
    // por defecto, el usuario debe estar autenticado para acceder a las rutas
    const requiresAuth = !to.meta.public;

    const requiredAuthority = to.meta.authority;
    const userIsLogged = getStore().state.user.logged;
    const loggedUserAuthority = getStore().state.user.authority;

    if (loggedUserAuthority == "USER_SUSPENDED" && !to.meta.isSuspendedPage) {
      // usuario suspendido, redirigir a página de suspensión
      next("/suspended");
    } else {
      if (requiresAuth) {
        // página privada
        if (userIsLogged) {
          if (requiredAuthority && requiredAuthority != loggedUserAuthority) {
            // usuario logueado pero sin permisos suficientes, le redirigimos a la página de login
            alert(
              "Acceso prohibido para el usuario actual; intenta autenticarte de nuevo"
            );
            auth.logout();
            next("/login");
          } else {
            // usuario logueado y con permisos adecuados
            next();
          }
        } else {
          // usuario no está logueado, no puede acceder a la página
          alert("Esta página requiere autenticación");
          next("/login");
        }
      } else {
        // página pública
        if (userIsLogged && to.meta.isLoginPage) {
          // si estamos logueados no hace falta volver a mostrar el login
          next({ name: "Home", replace: true });
        } else {
          next();
        }
      }
    }
  });
});

export default router;
