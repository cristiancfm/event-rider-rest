<template>
  <AppNavbar></AppNavbar>
  <div style="max-width: 1280px; min-height: calc(100vh - 92px); margin: auto">
    <!--  With the key, any change to the path will trigger a reload of the component,
   event if it is the same component being loaded -->
    <router-view :key="$route.fullPath" />
  </div>
  <AppFooter></AppFooter>
</template>

<script>
import AppNavbar from "@/components/AppNavbar";
import AppFooter from "@/components/AppFooter";

export default {
  components: { AppFooter, AppNavbar },
};
</script>

<style>
#app {
  font-family: Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

body.suspended-user-view {
  background: darkred;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
  cursor: pointer;
}

/* fixing popper warning in bootstrap 5.2: */
.dropdown-menu {
  margin-top: 0px !important;
}
</style>
