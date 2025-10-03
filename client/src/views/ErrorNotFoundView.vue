<template>
  <div>
    <div>
      <h1>404</h1>

      <h3>You tried to access to: {{ path }}</h3>

      <h2>There is nothing here!</h2>

      <router-link to="/" custom v-slot="{ href }">
        <a type="button" class="btn btn-secondary" :href="href"> Go Home </a>
      </router-link>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      path: this.$route.fullPath,
    };
  },
};
</script>

<style scoped>
h1 {
  font-size: 30vh;
}
h2,
h3 {
  opacity: 0.6;
}
</style>
