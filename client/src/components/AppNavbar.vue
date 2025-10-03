<template>
  <nav class="navbar navbar-expand-lg bg-light p-0" style="min-height: 50px">
    <div class="container-fluid">
      <router-link class="navbar-brand" to="/">
        <img
          src="../../public/event-rider-logo.png"
          alt="Event Rider"
          width="100"
        />
      </router-link>
      <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarMain"
        aria-controls="navbarMain"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarMain">
        <ul class="navbar-nav me-auto">
          <li class="nav-item">
            <router-link
              class="nav-link"
              to="/"
              aria-current="page"
              active-class="active"
            >
              Home
            </router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/events" active-class="active">
              Events
            </router-link>
          </li>
          <li class="nav-item">
            <router-link
              class="nav-link"
              to="/event-categories"
              active-class="active"
            >
              Categories
            </router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/members" active-class="active">
              Members
            </router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/about" active-class="active">
              About
            </router-link>
          </li>
        </ul>
        <div class="d-lg-flex me-auto me-lg-0">
          <ul class="navbar-nav">
            <li class="nav-item" v-if="!isLogged">
              <router-link
                class="btn btn-primary m-2"
                to="/login"
                active-class="active"
              >
                Log In
              </router-link>
            </li>
            <li class="nav-item" v-if="!isLogged">
              <router-link
                class="btn btn-primary m-2"
                to="/signup"
                active-class="active"
              >
                Sign Up
              </router-link>
            </li>
            <li class="nav-item" v-if="isLogged && isAdmin">
              <p class="text-secondary m-2 p-1">
                <i class="bi bi-person-badge-fill"></i>
                Admin Mode
              </p>
            </li>
            <li class="nav-item" v-if="isLogged && isVerified">
              <p class="text-secondary m-2 p-1">
                <i class="bi bi-patch-check-fill"></i>
                Verified
              </p>
            </li>
            <li class="nav-item" v-if="isLogged && isAdmin">
              <router-link
                class="btn btn-primary m-2"
                to="/admin"
                active-class="active"
              >
                Administration
              </router-link>
            </li>
            <li class="nav-item" v-if="isLogged">
              <router-link
                class="btn btn-primary m-2"
                to="/profile"
                active-class="active"
              >
                Profile
              </router-link>
            </li>
            <li class="nav-item" v-if="isLogged">
              <a class="btn btn-primary m-2" @click="logout()"> Log Out </a>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </nav>
</template>

<script>
import auth from "@/common/auth";
import { getStore } from "@/common/store";

export default {
  name: "AppNavbar",
  data() {
    return {
      store: getStore(),
    };
  },
  computed: {
    isLogged() {
      return this.store.state.user.logged;
    },
    isVerified() {
      return this.store.state.user.authority === "USER_VERIFIED";
    },
    isAdmin() {
      return this.store.state.user.authority === "ADMIN";
    },
  },
  methods: {
    logout() {
      auth.logout();
      // Después de hacer logout nos vamos a home
      this.$router.push("/");
    },
  },
};
</script>
